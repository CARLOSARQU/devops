# Guía de Instalación — QA Automation Mobile (Los Andes)

Documento de requisitos para ejecutar las pruebas automatizadas de la app Android de Caja Los Andes.
Cubre dos escenarios: **Docker con emulador** y **Linux con dispositivo físico**.

---

## Versiones requeridas

| Herramienta              | Versión          | Rol                                        |
|--------------------------|------------------|--------------------------------------------|
| Java JDK                 | 17 (mínimo)      | Compilar y ejecutar los tests              |
| Maven                    | 3.9.x            | Gestión de dependencias y ejecución        |
| Node.js                  | 20 LTS           | Requerido por Appium                       |
| Appium CLI               | 3.2.0            | Servidor de automatización mobile          |
| Appium driver UiAutomator2 | 7.0.0          | Control del dispositivo Android            |
| Android SDK cmdline-tools | latest          | Crear y gestionar emuladores               |
| Android SDK platform-tools | 36.0.2 (ADB)  | Comunicación USB / emulador con el sistema |
| Android Emulator          | latest          | Dispositivo virtual Android                |
| Android System Image      | API 33 (Android 13) | Sistema operativo del emulador        |

---

---

# OPCIÓN A — Docker con Emulador Android

## Requisitos previos del host

Antes de construir la imagen, la máquina host debe tener habilitada la virtualización por hardware (KVM),
que es lo que permite que el emulador Android corra a velocidad normal dentro del contenedor.

```bash
# Verificar que KVM está disponible en el host Linux
ls -la /dev/kvm

# Si no existe, habilitar virtualización en BIOS y luego:
sudo apt install qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils
sudo usermod -aG kvm $USER
# Reiniciar sesión después de este comando
```

## Dockerfile

Crear el archivo `Dockerfile` en la raíz del proyecto con el siguiente contenido:

```dockerfile
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$JAVA_HOME/bin:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator

# ── 1. Dependencias del sistema ───────────────────────────────────────────────
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    curl \
    unzip \
    wget \
    git \
    libgl1-mesa-glx \
    libpulse0 \
    libnss3 \
    libxcomposite1 \
    libxrandr2 \
    libxi6 \
    && rm -rf /var/lib/apt/lists/*

# ── 2. Node.js 20 LTS ─────────────────────────────────────────────────────────
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

# ── 3. Appium ─────────────────────────────────────────────────────────────────
RUN npm install -g appium@3.2.0
RUN appium driver install uiautomator2

# ── 4. Android SDK cmdline-tools ──────────────────────────────────────────────
RUN mkdir -p $ANDROID_HOME/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d $ANDROID_HOME/cmdline-tools && \
    mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# ── 5. Aceptar licencias e instalar platform-tools, emulator y system image ───
RUN yes | sdkmanager --licenses && \
    sdkmanager \
        "platform-tools" \
        "emulator" \
        "platforms;android-33" \
        "system-images;android-33;google_apis;x86_64"

# ── 6. Crear el emulador (AVD) ────────────────────────────────────────────────
RUN echo "no" | avdmanager create avd \
    --name "losandes_qa" \
    --package "system-images;android-33;google_apis;x86_64" \
    --device "pixel_4"

# ── 7. Directorio de trabajo ──────────────────────────────────────────────────
WORKDIR /qa-automation

# Copiar proyecto y descargar dependencias Maven en el build (capa cacheada)
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY . .
```

## Construir la imagen

```bash
docker build -t losandes-qa-automation .
```

## Ejecutar los tests

El contenedor necesita acceso a `/dev/kvm` del host para que el emulador tenga aceleración por hardware.

```bash
docker run --rm \
  --device /dev/kvm \
  -e JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \
  losandes-qa-automation \
  bash -c "
    # Iniciar emulador en segundo plano sin audio ni ventana gráfica
    emulator -avd losandes_qa -no-audio -no-window -no-snapshot &

    # Esperar a que el emulador esté listo (boot completado)
    adb wait-for-device
    adb shell 'while [[ -z \$(getprop sys.boot_completed) ]]; do sleep 3; done'
    echo 'Emulador listo'

    # Correr los tests
    mvn test \
      -Dsurefire.suiteXmlFiles=testng.xml \
      -Ddevice.name=emulator-5554
  "
```

## Verificar que el emulador arrancó correctamente

```bash
# Dentro del contenedor corriendo
adb devices
# Debe aparecer: emulator-5554   device
```

## Consideración importante sobre Keynua

> El filtro de verificación biométrica de **Keynua** requiere cámara real y comportamiento de dispositivo físico.
> Los tests que llegan hasta ese paso **no podrán completarse en el emulador**.
> Para tests de Keynua es obligatorio un dispositivo físico (ver Opción B).

---

---

# OPCIÓN B — Linux con Dispositivo Físico

Instalación directa sobre Ubuntu 22.04 con un celular Android conectado por USB.
Es la opción más simple cuando se dispone de un dispositivo físico.

## 1. Java 17

```bash
sudo apt update
sudo apt install openjdk-17-jdk -y

# Verificar
java -version
# openjdk version "17.x.x"

# Configurar variable de entorno
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
```

## 2. Maven

```bash
sudo apt install maven -y

# Verificar
mvn -version
# Apache Maven 3.x.x
```

## 3. Node.js 20 LTS

```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install nodejs -y

# Verificar
node -version   # v20.x.x
npm -version    # 10.x.x
```

## 4. Appium CLI

```bash
sudo npm install -g appium@3.2.0

# Verificar
appium -v
# 3.2.0
```

## 5. Driver UiAutomator2

```bash
appium driver install uiautomator2

# Verificar
appium driver list --installed
# uiautomator2@7.0.0 [installed (npm)]
```

## 6. Android SDK platform-tools (solo ADB, sin Android Studio)

```bash
# Descargar solo platform-tools
wget https://dl.google.com/android/repository/platform-tools-latest-linux.zip -O /tmp/platform-tools.zip
sudo unzip /tmp/platform-tools.zip -d /opt/android-sdk
rm /tmp/platform-tools.zip

# Configurar variables de entorno
echo 'export ANDROID_HOME=/opt/android-sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.bashrc
source ~/.bashrc

# Verificar
adb version
# Android Debug Bridge version 1.0.41
```

## 7. Configurar el celular Android

En el dispositivo físico activar:

```
Ajustes → Acerca del teléfono → Número de compilación (tocar 7 veces)
→ Opciones de desarrollador → Depuración USB: ON
```

Conectar el cable USB y confirmar el mensaje de confianza que aparece en el celular.

```bash
# Verificar que el celular es reconocido
adb devices
# Debe aparecer:
# ACXYVB4702000775   device
```

## 8. Clonar el repositorio de automatización

```bash
git clone <url-del-repo-qa-automation>
cd qa-automation
```

## 9. Ejecutar los tests

```bash
mvn test \
  -Dsurefire.suiteXmlFiles=testng.xml \
  -Ddevice.name=<serial_obtenido_con_adb_devices>

# Ejemplo con el serial actual:
mvn test \
  -Dsurefire.suiteXmlFiles=testng.xml \
  -Ddevice.name=ACXYVB4702000775
```

---

---

# Comparativa final

| Aspecto                    | Docker + Emulador        | Linux + Físico             |
|----------------------------|--------------------------|----------------------------|
| Dispositivo                | Emulador (virtual)       | Celular real por USB        |
| Requiere hardware especial | KVM en el host           | Puerto USB                  |
| Tests con Keynua           | No compatibles           | Compatibles                 |
| Mantenimiento              | Imagen versionada        | Instalación manual          |
| Velocidad del emulador     | Lenta sin KVM            | Velocidad real              |
| Recomendado para           | Tests sin biometría / CI | Tests completos end-to-end  |

---

## Checklist de verificación rápida

Antes de ejecutar los tests, correr estos comandos y confirmar que todos responden correctamente:

```bash
java -version          # 17+
mvn -version           # 3.9.x
node -version          # v20.x.x
appium -v              # 3.2.0
adb devices            # serial del dispositivo o emulator-5554
```
