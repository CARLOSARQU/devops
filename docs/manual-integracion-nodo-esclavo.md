# Integración QA Automation — Nodo Esclavo Jenkins

## Contexto

La PC de QA actúa como nodo esclavo (agent) conectado al Jenkins maestro de la empresa. Desde este nodo se ejecutan las pruebas automatizadas sobre dispositivos Android físicos conectados por USB, eliminando la necesidad de AWS Device Farm para ejecuciones locales.

```
Jenkins Master (AWS EC2)
        ↓  internet (puerto 50000)
PC QA — Jenkins Agent
        ↓  USB
Dispositivo Android
```

---

## Prerrequisitos

### En la PC de QA (nodo esclavo)

| Herramienta | Versión | Ruta |
|-------------|---------|------|
| Java JDK | 21 | `C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot` |
| Maven | 3.9.12 | `C:\maven\apache-maven-3.9.12` |
| Appium | 3.x | `C:\Users\[usuario]\AppData\Roaming\npm\node_modules\appium` |
| ADB | - | `C:\Users\[usuario]\AppData\Local\Android\Sdk\platform-tools` |
| Node.js | 20.x | `C:\Program Files\nodejs` |
| Git | - | `C:\Program Files\Git` |

### En el Jenkins Master
- Puerto `50000` abierto hacia la IP de la PC de QA (Security Group de EC2)
- Puerto `8080` accesible para la UI
- Permisos para crear nodos

---

## 1. Configurar el nodo en el Jenkins Master

```
Manage Jenkins → Nodes → New Node
  → Node name: qa-android-node
  → Type: Permanent Agent
  → OK
```

Configuración del nodo:

```
Description:          Nodo QA — ejecución de pruebas Android con dispositivo físico
Number of executors:  1
Remote root directory: C:\jenkins-agent
Labels:               android qa
Usage:                Only build jobs with label expressions matching this node
Launch method:        Launch agent by connecting it to the controller
Availability:         Keep this agent online as much as possible
```

Guardar y anotar el **secret** generado — se necesita para conectar el agente.

---

## 2. Conectar la PC de QA como agente

### Descargar el agent.jar

Desde el Jenkins Master, ir a:
```
Manage Jenkins → Nodes → qa-android-node → agent.jar
```

O descargarlo directamente:
```
http://[JENKINS_MASTER_URL]:8080/jnlpJars/agent.jar
```

Guardar en: `C:\jenkins-agent\agent.jar`

### Ejecutar el agente

Desde CMD en la PC de QA **como administrador**:

```cmd
java -jar C:\jenkins-agent\agent.jar ^
  -url http://[JENKINS_MASTER_URL]:8080 ^
  -secret [SECRET_GENERADO] ^
  -name qa-android-node ^
  -workDir C:\jenkins-agent
```

### Ejecutar como servicio de Windows (recomendado)

Para que el agente se inicie automáticamente sin intervención manual:

```cmd
sc create JenkinsAgent ^
  binPath= "java -jar C:\jenkins-agent\agent.jar -url http://[JENKINS_MASTER_URL]:8080 -secret [SECRET] -name qa-android-node -workDir C:\jenkins-agent" ^
  start= auto ^
  DisplayName= "Jenkins QA Agent"

sc start JenkinsAgent
```

---

## 3. Verificar la conexión

En el Jenkins Master:
```
Manage Jenkins → Nodes → qa-android-node
```

El nodo debe aparecer como **online** con el ícono verde.

---

## 4. Modificar el pipeline para usar el nodo

### Opción A — Todo el pipeline en el nodo QA

```groovy
pipeline {
    agent { label 'android' }

    environment {
        JAVA_HOME         = 'C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.10.7-hotspot'
        MAVEN_HOME        = 'C:\\maven\\apache-maven-3.9.12'
        PATH              = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${env.PATH}"
        APPIUM_EXECUTABLE = 'C:\\Users\\[usuario]\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js'
    }

    // ... resto del pipeline
}
```

### Opción B — Solo el stage QA en el nodo (recomendada)

Mantiene el pipeline existente en el master y solo delega la ejecución de tests al nodo QA:

```groovy
stage('QA Automation') {
    agent { label 'android' }
    steps {
        script {
            // Checkout del repo QA
            git branch: 'main',
                url: 'https://github.com/CARLOSARQU/devops.git'

            // Verificar dispositivo conectado
            def devices = bat(script: 'adb devices', returnStdout: true).trim()
            echo "Dispositivos: ${devices}"
            if (!devices.contains('device')) {
                error('No hay dispositivos Android conectados.')
            }

            // Ejecutar tests
            bat """
                mvn clean test ^
                -Denv=${params.ENV} ^
                -Dcucumber.filter.tags="${params.TEST_TAGS}" ^
                -Dallure.results.directory=target/allure-results
            """
        }
    }
    post {
        always {
            allure([
                includeProperties: false,
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}
```

---

## 5. Parámetros del pipeline

Agregar al bloque `parameters`:

```groovy
parameters {
    choice(
        name: 'TEST_TAGS',
        choices: ['@smoke', '@regression', '@login_test', '@login_mainframe'],
        description: 'Tags de Cucumber a ejecutar'
    )
    string(
        name: 'ENV',
        defaultValue: 'cert',
        description: 'Entorno de ejecución (cert / dev)'
    )
}
```

---

## 6. Requisitos de red

| Puerto | Dirección | Descripción |
|--------|-----------|-------------|
| `50000` | Master → PC QA | Comunicación Jenkins agent |
| `8080` | PC QA → Master | UI y API Jenkins |

Abrir en el Security Group de la instancia EC2 del Jenkins Master:
```
Inbound rule:
  Type: Custom TCP
  Port: 50000
  Source: IP de la PC QA
```

---

## 7. Consideraciones

- El servicio Jenkins en la PC de QA debe correr con el **usuario de Windows del QA** (no como SYSTEM) para tener acceso a Appium, ADB y demás herramientas instaladas en el perfil del usuario
- El dispositivo Android debe estar conectado por USB con **depuración USB habilitada**
- Si la IP de la PC QA cambia (DHCP), actualizar la regla del Security Group o asignar IP fija
- El `workDir` (`C:\jenkins-agent`) debe existir antes de iniciar el agente — crearlo manualmente si no existe

---

## 8. Verificación rápida

Desde la PC QA, antes de conectar el agente:

```cmd
java -version
mvn -version
adb devices
where appium
```

Todo debe responder correctamente. Si alguno falla, revisar el PATH del sistema.
