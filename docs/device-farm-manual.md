# Manual de Ejecución en AWS Device Farm

## Requisitos previos

- AWS CLI instalado
- Cuenta AWS con acceso a Device Farm
- Usuario IAM con política `AmazonDeviceFarmFullAccess`
- Java 17+ y Maven instalados
- Proyecto de automatización con TestNG + Appium

---

## 1. Instalación y configuración de AWS CLI

### Instalar AWS CLI
```powershell
winget install Amazon.AWSCLI
```

### Crear usuario IAM (no usar root)
```
AWS Console → IAM → Users → Create User
  → Nombre: qa-automation-cli
  → Adjuntar política: AmazonDeviceFarmFullAccess
  → Security credentials → Create access key → CLI
  → Guardar Access Key ID y Secret Access Key
```

### Configurar AWS CLI
```bash
aws configure
  AWS Access Key ID: [tu clave]
  AWS Secret Access Key: [tu clave secreta]
  Default region: us-west-2
  Default output format: json
```

> **Importante:** Device Farm solo está disponible en `us-west-2` (Oregon), sin importar la región del resto de la infraestructura.

### Verificar configuración
```bash
aws sts get-caller-identity
```

---

## 2. Preparar el proyecto

### Compilar y empaquetar los tests
```bash
mvn clean package -DskipTests
```

Esto genera: `target/zip-with-dependencies.zip`

---

## 3. Configurar Device Farm

### 3.1 Obtener el ARN del proyecto
```bash
aws devicefarm list-projects --region us-west-2
```

### 3.2 Crear device pool con un solo dispositivo

Listar dispositivos Android disponibles (desde CMD):
```cmd
aws devicefarm list-devices --region us-west-2 --filters "[{\"attribute\":\"PLATFORM\",\"operator\":\"EQUALS\",\"values\":[\"ANDROID\"]}]"
```

Crear el device pool:
```cmd
aws devicefarm create-device-pool ^
  --project-arn [PROJECT_ARN] ^
  --name "qa-pixel8a" ^
  --rules "[{\"attribute\":\"ARN\",\"operator\":\"IN\",\"value\":\"[\\\"[DEVICE_ARN]\\\"]\"}]" ^
  --region us-west-2
```

> **Nota:** No usar `--max-devices` cuando se especifica un ARN concreto.

---

## 4. Subir artefactos a Device Farm

Device Farm requiere tres artefactos:
1. APK de la app
2. ZIP con los tests
3. testspec.yml

### 4.1 Subir el APK

**Paso 1 — Registrar el upload:**
```bash
aws devicefarm create-upload \
  --project-arn [PROJECT_ARN] \
  --name app.apk \
  --type ANDROID_APP \
  --region us-west-2
```

Guarda el `arn` y la `url` que devuelve.

**Paso 2 — Subir el archivo (PowerShell):**
```powershell
$url = "[URL_DEVUELTA]"
$file = "C:\ruta\a\app.apk"
Invoke-WebRequest -Uri $url -Method Put -InFile $file -UseBasicParsing
```

**Paso 3 — Verificar:**
```bash
aws devicefarm get-upload --arn [APK_ARN]
# status debe ser SUCCEEDED
```

---

### 4.2 Subir el ZIP de tests

**Paso 1 — Registrar:**
```bash
aws devicefarm create-upload \
  --project-arn [PROJECT_ARN] \
  --name tests.zip \
  --type APPIUM_JAVA_TESTNG_TEST_PACKAGE \
  --region us-west-2
```

**Paso 2 — Subir (PowerShell):**
```powershell
$url2 = "[URL_DEVUELTA]"
$zip = "C:\QA\DEVOPS\qa-automation\target\zip-with-dependencies.zip"
Invoke-WebRequest -Uri $url2 -Method Put -InFile $zip -UseBasicParsing
```

**Paso 3 — Verificar:**
```bash
aws devicefarm get-upload --arn [TEST_PACKAGE_ARN]
# status debe ser SUCCEEDED
```

---

### 4.3 Subir el testspec.yml

**Paso 1 — Registrar:**
```bash
aws devicefarm create-upload \
  --project-arn [PROJECT_ARN] \
  --name testspec.yml \
  --type APPIUM_JAVA_TESTNG_TEST_SPEC \
  --region us-west-2
```

**Paso 2 — Subir (PowerShell):**
```powershell
$urlSpec = "[URL_DEVUELTA]"
$spec = "C:\QA\DEVOPS\qa-automation\testspec.yml"
Invoke-WebRequest -Uri $urlSpec -Method Put -InFile $spec -UseBasicParsing
```

**Paso 3 — Verificar:**
```bash
aws devicefarm get-upload --arn [TEST_SPEC_ARN]
# status debe ser SUCCEEDED
```

---

## 5. Lanzar el run

Desde CMD (las comillas funcionan mejor que en PowerShell):

```cmd
aws devicefarm schedule-run ^
  --project-arn [PROJECT_ARN] ^
  --app-arn [APK_ARN] ^
  --device-pool-arn [DEVICE_POOL_ARN] ^
  --name "qa-run-01" ^
  --test "{\"type\":\"APPIUM_JAVA_TESTNG\",\"testPackageArn\":\"[TEST_PACKAGE_ARN]\",\"testSpecArn\":\"[TEST_SPEC_ARN]\"}" ^
  --configuration "{\"jobTimeoutMinutes\":30,\"environmentVariables\":[{\"key\":\"TEST_TAGS\",\"value\":\"@smoke\"}]}" ^
  --region us-west-2
```

### Cambiar el tag a ejecutar
Modificá el valor de `TEST_TAGS` en `--configuration`:
- `@smoke` — pruebas rápidas
- `@regression` — regresión completa
- `@login_test` — solo login
- `@login_mainframe` — solo mainframe

> Si no se envía `TEST_TAGS`, el `testspec.yml` usa `@regression` por defecto.

---

## 6. Monitorear el run

```bash
aws devicefarm get-run --arn [RUN_ARN] --region us-west-2
```

Estados posibles:
| Estado | Descripción |
|--------|-------------|
| `SCHEDULING` | En cola |
| `PENDING` | Esperando dispositivo |
| `RUNNING` | Ejecutando |
| `COMPLETED` | Finalizado |

---

## 7. ARNs del proyecto (referencia rápida)

| Recurso | ARN |
|---------|-----|
| Proyecto | `arn:aws:devicefarm:us-west-2:066288112450:project:b576551b-0ab0-4542-a4ed-4b0e84a7c553` |
| Device Pool (Pixel 8a) | `arn:aws:devicefarm:us-west-2:066288112450:devicepool:b576551b-0ab0-4542-a4ed-4b0e84a7c553/c2d07a6b-23db-4cf3-a120-572490f80c2c` |

---

## 8. Integración con Jenkins (futuro)

El stage a agregar en el pipeline de la empresa:

```groovy
stage('QA Automation') {
    steps {
        // Compilar tests
        bat 'mvn clean package -DskipTests'

        // Subir ZIP a Device Farm
        script {
            def upload = bat(
                script: """aws devicefarm create-upload ^
                    --project-arn %PROJECT_ARN% ^
                    --name tests.zip ^
                    --type APPIUM_JAVA_TESTNG_TEST_PACKAGE ^
                    --region us-west-2""",
                returnStdout: true
            )
            // Subir archivo y lanzar run con TEST_TAGS del parámetro
            bat """aws devicefarm schedule-run ^
                --project-arn %PROJECT_ARN% ^
                --app-arn %APP_ARN% ^
                --device-pool-arn %DEVICE_POOL_ARN% ^
                --name "qa-run-${env.BUILD_NUMBER}" ^
                --test "{...testPackageArn, testSpecArn...}" ^
                --configuration "{...TEST_TAGS: ${params.TEST_TAGS}...}" ^
                --region us-west-2
            """
        }
    }
}
```

> El APK lo toma directamente del stage `Publish` anterior que ya lo sube a S3 — Device Farm lo descarga desde ahí.

---

## Notas importantes

- Las URLs presignadas de Device Farm expiran en **24 horas** — si falla el upload, regenerar con `create-upload`
- Device Farm solo existe en `us-west-2` — siempre especificar `--region us-west-2`
- Usar **CMD** para comandos con JSON en `--test` y `--configuration` — PowerShell tiene problemas con las comillas
- Usar **PowerShell** para subir archivos con `Invoke-WebRequest`
- El ZIP de tests debe generarse con `mvn clean package -DskipTests` — genera `zip-with-dependencies.zip`
