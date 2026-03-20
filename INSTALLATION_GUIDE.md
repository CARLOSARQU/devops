# Guía de Instalación — QA Automation Mobile
### Para ejecutar en Ubuntu 22.04 LTS (PC desde cero)

---

## Lista de todo lo que se necesita instalar

| # | Herramienta                  | Versión        | Para qué sirve                               |
|---|------------------------------|----------------|----------------------------------------------|
| 1 | Git                          | latest         | Descargar el código del repositorio          |
| 2 | Java JDK                     | 21             | Compilar y ejecutar los tests                |
| 3 | Maven                        | 3.9.x          | Gestionar dependencias y correr los tests    |
| 4 | Node.js                      | 24             | Requerido por Appium para funcionar          |
| 5 | Appium                       | 3.2.0          | Servidor que controla el dispositivo         |
| 6 | Appium UiAutomator2          | 7.0.0          | Driver para dispositivos Android             |
| 7 | Android SDK platform-tools   | 36.0.2 (ADB)   | Comunicación entre la PC y el dispositivo    |
| 8 | Android SDK cmdline-tools    | latest         | Solo si se usa emulador                      |
| 9 | Android Build Tools          | 33.0.0         | Requerido para instalar APKs (aapt2)         |
| 10| Android Emulator             | latest         | Solo si se usa emulador                      |
| 11| Android System Image API 33  | Android 13     | Solo si se usa emulador                      |

---

## Paso a paso

### Paso 1 — Actualizar el sistema e instalar dependencias base

```bash
apt update && apt upgrade -y
apt install -y curl wget unzip
```

---

### Paso 2 — Git

**Descarga:** https://git-scm.com/downloads/linux

```bash
apt install git -y

# Verificar
git --version
# git version 2.x.x
```

---

### Paso 3 — Java 21 JDK

**Descarga:** https://adoptium.net/temurin/releases/?version=21

```bash
apt install openjdk-21-jdk -y

# Verificar
java -version
# openjdk version "21.x.x"
```

Configurar la variable de entorno:

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
```

---

### Paso 4 — Maven

**Descarga:** https://maven.apache.org/download.cgi

```bash
apt install maven -y

# Verificar
mvn -version
# Apache Maven 3.x.x
```

---

### Paso 5 — Node.js 24

**Descarga:** https://nodejs.org/en/download

```bash
curl -fsSL https://deb.nodesource.com/setup_24.x | bash -
apt install nodejs -y

# Verificar
node --version
# v24.x.x

npm -version
# 10.x.x
```

---

### Paso 6 — Appium

**Documentación:** https://appium.io/docs/en/latest/quickstart/install/

```bash
npm install -g appium@3.2.0

# Verificar
appium -v
# 3.2.0
```

---

### Paso 7 — Appium Driver UiAutomator2

**Documentación:** https://appium.io/docs/en/latest/quickstart/uiauto2-driver/

```bash
appium driver install uiautomator2

# Verificar
appium driver list --installed
# uiautomator2@7.0.0 [installed (npm)]
```

---

### Paso 8 — Android SDK platform-tools (ADB)

**Descarga:** https://developer.android.com/studio/releases/platform-tools

```bash
# Descargar platform-tools
wget https://dl.google.com/android/repository/platform-tools-latest-linux.zip -O /tmp/platform-tools.zip

# Descomprimir
unzip /tmp/platform-tools.zip -d /opt/android-sdk
rm /tmp/platform-tools.zip

# Configurar variables de entorno
echo 'export ANDROID_HOME=/opt/android-sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.bashrc
source ~/.bashrc

# Verificar
adb version
# Android Debug Bridge version 1.0.41
```

---

## Opción A — Dispositivo físico (celular por USB)

### Paso 9A — Instalar Android Build Tools

Requerido para que Appium pueda leer e instalar el APK en el dispositivo.

```bash
# Instalar cmdline-tools primero (para usar sdkmanager)
mkdir -p /opt/android-sdk/cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip
unzip /tmp/cmdline-tools.zip -d /opt/android-sdk/cmdline-tools
mv /opt/android-sdk/cmdline-tools/cmdline-tools /opt/android-sdk/cmdline-tools/latest
rm /tmp/cmdline-tools.zip

echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.bashrc
source ~/.bashrc

# Instalar build-tools
yes | sdkmanager --licenses
sdkmanager "build-tools;33.0.0"

# Verificar
ls /opt/android-sdk/build-tools/33.0.0/aapt2
```

### Paso 10A — Configurar el celular Android

En el dispositivo físico:

```
1. Ir a:  Ajustes → Acerca del teléfono → Número de compilación
2. Tocar "Número de compilación" 7 veces seguidas
3. Volver a Ajustes → Opciones de desarrollador
4. Activar: Depuración USB → ON
5. Conectar el cable USB a la PC
6. En el celular aparecerá un mensaje: "¿Permitir depuración USB?" → Aceptar
```

Verificar que la PC reconoce el celular:

```bash
adb devices
# Debe aparecer el serial del dispositivo, ejemplo:
# ACXYVB4702000775   device
```

### Paso 10A — Clonar el repositorio y configurar

```bash
git clone <URL-del-repositorio-qa-automation>
cd qa-automation
```

Abrir el archivo `src/test/resources/cert.properties` y completar los siguientes valores:

```properties
# Reemplazar con el serial que devolvió "adb devices"
device.name=SERIAL_DEL_CELULAR

# Credenciales de la cuenta de pruebas de la app
test.dni=DNI_DE_PRUEBA
test.password=CLAVE_DE_PRUEBA
```

### Paso 11A — Ejecutar los tests

```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

---

## Opción B — Emulador Android (dispositivo virtual)

> **Requisito previo:** la PC o servidor debe tener KVM habilitado.
> KVM es la tecnología que permite que el emulador corra a velocidad normal.
> Sin KVM el emulador es demasiado lento para ejecutar los tests.
>
> Verificar que KVM está disponible:
> ```bash
> ls /dev/kvm
> # Si el archivo existe → KVM disponible ✅
> # Si no existe        → KVM no disponible ❌ (activar en BIOS/UEFI)
> ```

### Paso 9B — Instalar Android SDK cmdline-tools

**Descarga:** https://developer.android.com/studio/releases/command-line-tools

```bash
# Crear carpeta para las herramientas
mkdir -p /opt/android-sdk/cmdline-tools

# Descargar cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip

# Descomprimir
unzip /tmp/cmdline-tools.zip -d /opt/android-sdk/cmdline-tools
mv /opt/android-sdk/cmdline-tools/cmdline-tools /opt/android-sdk/cmdline-tools/latest
rm /tmp/cmdline-tools.zip

# Agregar al PATH
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/emulator' >> ~/.bashrc
source ~/.bashrc

# Verificar
sdkmanager --version
```

### Paso 10B — Instalar emulador y system image Android 13

```bash
# Aceptar licencias
yes | sdkmanager --licenses

# Instalar emulador y sistema operativo Android 13 (API 33)
sdkmanager "emulator" "platforms;android-33" "build-tools;33.0.0" "system-images;android-33;google_apis;x86_64"

# Verificar
sdkmanager --list_installed
```

### Paso 11B — Crear el dispositivo virtual (AVD)

```bash
echo "no" | avdmanager create avd \
  --name "losandes_qa" \
  --package "system-images;android-33;google_apis;x86_64" \
  --device "pixel_4"

# Verificar que el AVD fue creado
avdmanager list avd
# Name: losandes_qa
```

### Paso 12B — Arrancar el emulador

```bash
# Iniciar el emulador en segundo plano
emulator -avd losandes_qa -no-audio -no-window &

# Esperar a que el emulador termine de iniciar
adb wait-for-device
adb shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 3; done'
echo "Emulador listo"

# Verificar que aparece como dispositivo
adb devices
# emulator-5554   device
```

### Paso 13B — Clonar el repositorio y configurar

```bash
git clone <URL-del-repositorio-qa-automation>
cd qa-automation
```

Abrir el archivo `src/test/resources/cert.properties` y completar:

```properties
# Para emulador el device.name siempre es este valor
device.name=emulator-5554

# Credenciales de la cuenta de pruebas de la app
test.dni=DNI_DE_PRUEBA
test.password=CLAVE_DE_PRUEBA
```

### Paso 14B — Ejecutar los tests

```bash
mvn test -Dsurefire.suiteXmlFiles=testng.xml
```

---

## Verificación rápida — checklist final

Antes de correr los tests, confirmar que todos estos comandos responden correctamente:

```bash
git --version          # git version 2.x.x            ✅
java -version          # openjdk version "21.x.x"      ✅
mvn -version           # Apache Maven 3.x.x            ✅
node -version          # v24.x.x                       ✅
npm -version           # 10.x.x                        ✅
appium -v              # 3.2.0                          ✅
appium driver list --installed  # uiautomator2@7.0.0   ✅
adb devices            # serial del celular o emulator  ✅
```

---

## Comparativa — físico vs emulador

| Aspecto                    | Dispositivo físico          | Emulador                        |
|----------------------------|-----------------------------|---------------------------------|
| Requiere celular           | Sí, conectado por USB       | No                              |
| Requiere KVM               | No                          | Sí (en la PC o servidor)        |
| Velocidad                  | Real                        | Depende del hardware del host   |
| Tests con Keynua           | ✅ Compatibles              | ❌ No compatibles               |
| Recomendado para           | Tests completos end-to-end  | Tests sin biometría / CI-CD     |

---

## Capturas de pantalla en caso de error

Cuando un test falla, el framework guarda automáticamente una captura de pantalla del dispositivo en ese momento.

**Ubicación de las capturas:**
```
qa-automation/
└── target/
    └── screenshots/
          └── NombreDelTest_20260318143022.png
```

### Acceso según el entorno

**Ejecución local (PC directa):**
```bash
# Las capturas quedan en la carpeta target/ del proyecto
ls target/screenshots/
```

**Ejecución en Jenkins:**

Las capturas se archivan automáticamente en Jenkins y se pueden ver desde la interfaz web:
```
Jenkins → Pipeline → Build #X → Artifacts → target/screenshots/
```

Esto funciona gracias a esta configuración en el pipeline:
```groovy
post {
    always {
        archiveArtifacts artifacts: 'target/screenshots/**/*.png',
                         allowEmptyArchive: true
    }
}
```

**Ejecución en Docker:**

Dentro del contenedor las capturas quedan en `target/screenshots/`, pero al apagarse el contenedor se pierden. Para conservarlas hay que montar una carpeta del host al correr el contenedor:

```bash
docker run \
  --device /dev/kvm \
  -v /ruta/local/screenshots:/qa-automation/target/screenshots \
  losandes-qa-automation
```

Así las capturas quedan guardadas en la PC aunque el contenedor se apague.

---

## Notas para arquitectura

- **No se necesita Android Studio** — solo los módulos específicos del SDK. Reduce el espacio en disco requerido.
- **No se necesita Appium corriendo como servicio** — el propio código Java lo levanta y apaga automáticamente al correr los tests.
- **Espacio en disco estimado:**
  - Solo dispositivo físico: ~500 MB
  - Con emulador incluido: ~3 GB (system image Android 13 pesa ~1.5 GB)
- **KVM es obligatorio para el emulador** — sin él el emulador es inusable para automatización.
