# QA Automation — Arquitectura del Proyecto

## Tabla de contenidos
1. [Stack tecnológico](#stack-tecnológico)
2. [Estructura del proyecto](#estructura-del-proyecto)
3. [Capas y su conexión](#capas-y-su-conexión)
4. [Modos de ejecución](#modos-de-ejecución)
5. [Ciclo de vida de un test](#ciclo-de-vida-de-un-test)
6. [Tags de Cucumber](#tags-de-cucumber)
7. [Configuración](#configuración)
8. [Cómo ejecutar las pruebas](#cómo-ejecutar-las-pruebas)

---

## Stack tecnológico

| Tecnología | Versión | Rol |
|---|---------|---|
| Java | 17+     | Lenguaje principal |
| Appium Java Client | 8.6.0   | Driver móvil Android |
| Selenium | 4.13.0  | Base del driver |
| Cucumber | 7.15.0  | Framework BDD |
| TestNG | 7.9.0   | Runner de tests |
| Log4j | 2.25.3  | Logging |
| Maven | —       | Build y empaquetado |

---

## Estructura del proyecto

```
qa-automation/
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   └── BaseScreen.java             # Clase base con métodos comunes de interacción
│   │   ├── drivers/
│   │   │   └── DriverManager.java          # Gestión del driver (Local / BrowserStack / DeviceFarm)
│   │   └── utils/
│   │       ├── ConfigReader.java           # Lector de properties
│   │       └── TestListener.java           # Listener TestNG (logs de resultado)
│   │
│   └── test/
│       ├── java/
│       │   ├── hooks/Hooks.java            # Ciclo de vida del test (@BeforeAll, @Before, @After, @AfterAll)
│       │   ├── runners/TestRunner.java     # Configuración de Cucumber (@CucumberOptions)
│       │   └── screens/
│       │       ├── ScenarioContext.java    # Estado compartido entre steps
│       │       ├── CommonSteps.java        # Steps reutilizables (login, navegación)
│       │       ├── onboarding/
│       │       │   ├── WelcomeScreen.java
│       │       │   └── WelcomeActions.java
│       │       ├── login/
│       │       │   ├── LoginScreen.java
│       │       │   ├── LoginActions.java
│       │       │   └── LoginSteps.java
│       │       ├── home/
│       │       │   ├── HomeScreen.java
│       │       │   ├── HomeActions.java
│       │       │   └── HomeSteps.java
│       │       ├── operations/
│       │       │   ├── OperationMenuScreen.java
│       │       │   ├── OperationMenuActions.java
│       │       │   ├── TransferMenuScreen.java
│       │       │   ├── TransferMenuActions.java
│       │       │   ├── PayCreditMenuScreen.java
│       │       │   └── PayCreditMenuActions.java
│       │       ├── transfers/
│       │       │   ├── own/                # Transferencia entre cuentas propias
│       │       │   │   ├── OwnTransferDetailsScreen.java
│       │       │   │   ├── OwnTransferDetailsActions.java
│       │       │   │   ├── OwnTransferSummaryScreen.java
│       │       │   │   ├── OwnTransferSummaryActions.java
│       │       │   │   ├── OwnTransferReceiptScreen.java
│       │       │   │   ├── OwnTransferReceiptActions.java
│       │       │   │   └── OwnTransferSteps.java
│       │       │   └── losandes/           # Transferencia a otras cuentas Los Andes
│       │       │       ├── LosAndesAccountEntryScreen.java
│       │       │       ├── LosAndesAccountEntryActions.java
│       │       │       ├── LosAndesDetailsScreen.java
│       │       │       ├── LosAndesDetailsActions.java
│       │       │       ├── LosAndesTransferSummaryScreen.java
│       │       │       ├── LosAndesTransferSummaryActions.java
│       │       │       ├── LosAndesOtpScreen.java
│       │       │       ├── LosAndesOtpActions.java
│       │       │       ├── LosAndesTransferReceiptScreen.java
│       │       │       ├── LosAndesTransferReceiptActions.java
│       │       │       └── ThirdPartyTransferSteps.java
│       │       └── credit/                 # Pago de cuota de crédito
│       │           ├── CreditSelectionScreen.java
│       │           ├── CreditSelectionActions.java
│       │           ├── CreditMenuScreen.java
│       │           ├── CreditMenuActions.java
│       │           ├── AccountSelectionScreen.java
│       │           ├── AccountSelectionActions.java
│       │           ├── CreditPaymentSummaryScreen.java
│       │           ├── CreditPaymentSummaryActions.java
│       │           ├── CreditPaymentReceiptScreen.java
│       │           ├── CreditPaymentReceiptActions.java
│       │           └── CreditPaymentSteps.java
│       └── resources/
│           ├── cert.properties             # Configuración local
│           ├── dev.properties
│           ├── prod.properties
│           └── features/
│               ├── login.feature
│               ├── home.feature
│               ├── own_transfer.feature
│               ├── third_party_transfer.feature
│               └── credit_payment.feature
│
├── testng.xml                              # Suite TestNG
├── testspec.yml                            # Especificación para AWS Device Farm
└── pom.xml
```

---

## Capas y su conexión

El framework tiene 5 capas que se comunican en cascada:

```
┌─────────────────────────────────────────┐
│           Feature Files (.feature)       │  ← Define el comportamiento en lenguaje natural
│   Given / When / Then / And             │
└────────────────┬────────────────────────┘
                 │  Cucumber resuelve cada step por texto
                 ▼
┌─────────────────────────────────────────┐
│           Steps (XxxSteps.java)          │  ← Traducen el lenguaje natural a código Java
│   @Given  @When  @Then  @And            │
│   Usan ScenarioContext para             │
│   compartir objetos entre steps         │
└────────────────┬────────────────────────┘
                 │  Llaman métodos de negocio
                 ▼
┌─────────────────────────────────────────┐
│           Actions (XxxActions.java)      │  ← Lógica de flujo y navegación entre pantallas
│   isLoaded(), clickX() → YActions      │
│   Extienden su Screen correspondiente  │
└────────────────┬────────────────────────┘
                 │  Heredan los locators
                 ▼
┌─────────────────────────────────────────┐
│           Screens (XxxScreen.java)       │  ← Solo locators, sin lógica
│   @AndroidFindBy(uiAutomator = ...)    │
│   Todos extienden BaseScreen           │
└────────────────┬────────────────────────┘
                 │  Usan el driver para interactuar
                 ▼
┌─────────────────────────────────────────┐
│           DriverManager                  │  ← Provee el AndroidDriver
│   Local / BrowserStack / DeviceFarm    │
└─────────────────────────────────────────┘
```

### Cadena de herencia

```
XxxSteps → XxxActions → XxxScreen → BaseScreen
```

Cada módulo agrupa sus tres capas en el mismo paquete:

```
screens/login/
  LoginScreen.java    ← @AndroidFindBy locators
  LoginActions.java   ← extends LoginScreen, métodos de negocio
  LoginSteps.java     ← @Given/@When/@Then, usa LoginActions
```

### ScenarioContext — estado compartido entre steps

`ScenarioContext` actúa como un contenedor que permite pasar objetos Actions entre distintos steps del mismo escenario sin necesitar constructores ni parámetros:

```
CommonSteps          →  context.homePage        →  HomeSteps
CommonSteps          →  context.operationMenu   →  CreditPaymentSteps
CommonSteps          →  context.transferMenu    →  OwnTransferSteps / ThirdPartyTransferSteps
```

El flag `isLoggedIn` es estático y persiste entre escenarios para controlar si se debe hacer reset completo de la app o solo reiniciarla.

### Hooks — ciclo de vida del test

```
@BeforeAll  → Inicia Appium + crea driver (una sola vez por suite)
@Before     → Evalúa si limpiar la app (resetApp) o solo reiniciarla (restartApp)
@After      → Si falla: screenshot + marca sesión como inválida
@AfterAll   → Cierra driver + apaga Appium
```

**Lógica de reset en `@Before`:**

```
¿Tiene tag @login_test?  →  resetApp()  (siempre limpia — estos tests prueban el login en sí)
¿isLoggedIn = false?     →  resetApp()  (primer escenario o tras un fallo)
¿isLoggedIn = true?      →  restartApp()  (preserva sesión, ahorra ~10 seg)
```

---

## Modos de ejecución

El `DriverManager` detecta el modo automáticamente:

| Modo | Cómo se detecta | Appium |
|---|---|---|
| **Local** | Default | Se inicia internamente en puerto libre |
| **BrowserStack** | `execution.mode=browserstack` en properties | Remoto (BrowserStack cloud) |
| **AWS Device Farm** | Variable de entorno `DEVICEFARM_DEVICE_UDID` presente | Gestionado por Device Farm |

### Local
- Appium se levanta solo al iniciar la suite
- El dispositivo se toma de `cert.properties` (`device.name`)
- Si Jenkins inyecta `-Dapp.apk.path`, instala el APK; si no, usa `app.package` / `app.activity`

### BrowserStack
- Configurar en `cert.properties`: `execution.mode=browserstack` + credenciales
- No requiere dispositivo físico ni Appium local

### AWS Device Farm
- No requiere configuración local; todo viene de variables de entorno del entorno Device Farm
- Se empaqueta con `mvn clean package -DskipTests` → genera `target/zip-with-dependencies.zip`
- El `testspec.yml` controla la instalación de Appium 3, arranque y ejecución

---

## Ciclo de vida de un test

Ejemplo con `own_transfer.feature`:

```
1. @BeforeAll
   └── Appium inicia → driver creado

2. @Before (Scenario: Transferencia entre mis cuentas)
   ├── ScenarioContext.reset()  → limpia objetos del escenario anterior
   ├── isLoggedIn = false       → resetApp() (limpia datos de la app, espera 15s)
   └── isLoggedIn = true        → restartApp() (solo reinicia, espera 5s)

3. Background: "Given el usuario ha iniciado sesión"
   ├── Si isLoggedIn = true: inicializa HomePage directamente
   └── Si isLoggedIn = false: WelcomePage → LoginPage → HomePage → isLoggedIn = true

4. Steps del escenario:
   When navego a Operaciones        → HomeActions.irAOperaciones() → OperationMenuActions
   And selecciono Transferencias    → OperationMenuActions.clickTransferencias() → TransferMenuActions
   And selecciono Entre mis cuentas → TransferMenuActions.clickEntreMisCuentas() → OwnTransferDetailsActions
   Then la pantalla de detalles ... → OwnTransferDetailsActions.isLoaded()
   When ingreso el monto "100"      → OwnTransferDetailsActions.enterAmountAndContinue() → OwnTransferSummaryActions
   Then la pantalla de resumen ...  → OwnTransferSummaryActions.isLoaded()
   When confirmo la transferencia   → OwnTransferSummaryActions.clickContinue() → OwnTransferReceiptActions
   Then el comprobante aparece      → OwnTransferReceiptActions.isTransferenciaExitosa()

5. @After
   ├── PASS → log éxito, isLoggedIn permanece true
   └── FAIL → screenshot guardado en target/screenshots/, isLoggedIn = false
```

---

## Tags de Cucumber

Los tags se definen en los `.feature` y controlan qué escenarios se ejecutan.

### Tags disponibles

| Tag | Escenarios incluidos |
|---|---|
| `@regression` | Todos (está en el nivel Feature de todos los archivos) |
| `@login_mainframe` | Home carga correctamente + Login Exitoso |
| `@own_transfer` | Transferencia entre cuentas propias |
| `@third_party_transfer` | Transferencia a otras cuentas Los Andes |
| `@credit_payment` | Pago de cuota de crédito |
| `@smoke` | Shortcuts visibles, toggle saldo, credenciales inválidas |
| `@login_test` | Técnico: fuerza `resetApp()` en `@Before` |

### Herencia de tags

```gherkin
@regression                    ← todos los escenarios del archivo heredan @regression
Feature: Pantalla Home

  @login_mainframe             ← solo este escenario tiene @login_mainframe
  Scenario: Home carga correctamente

  @smoke                       ← solo estos tienen @smoke
  Scenario: Shortcuts visibles
```

---

## Configuración

### cert.properties (local)

```properties
# Modo de ejecución: local | browserstack
execution.mode=local

# Dispositivo
platform.name=Android
device.name=<UDID_DEL_DISPOSITIVO>
app.package=com.losandes.bancamovil.qa
app.activity=com.losandes.bancamovil.MainActivity
automation.name=UIAutomator2
no.reset=false

# BrowserStack (solo si execution.mode=browserstack)
browserstack.username=
browserstack.access.key=
browserstack.app.url=
browserstack.device=
browserstack.os.version=

# Credenciales de prueba
test.dni=
test.password=
test.third.party.account=
```

### Cambiar entorno

```bash
mvn test -Denv=dev    # usa dev.properties
mvn test -Denv=prod   # usa prod.properties
# sin -Denv           # usa cert.properties (default)
```

---

## Cómo ejecutar las pruebas

### Prerrequisitos

- Java 17
- Maven
- Android Studio / ADB instalado
- Dispositivo físico o emulador conectado (modo local)
- Node.js + Appium instalado globalmente: `npm install -g appium`
- Driver UiAutomator2: `appium driver install uiautomator2`

### Ejecución local

```bash
# Todos los tests de regresión
mvn test

# Solo un tag específico
mvn test "-Dcucumber.filter.tags=@login_mainframe"
mvn test "-Dcucumber.filter.tags=@own_transfer"
mvn test "-Dcucumber.filter.tags=@credit_payment"
mvn test "-Dcucumber.filter.tags=@third_party_transfer"

# Regresión completa excluyendo smoke
mvn test "-Dcucumber.filter.tags=@regression and not @smoke"

# Combinación de tags
mvn test "-Dcucumber.filter.tags=@login_mainframe or @own_transfer"
```

> **PowerShell:** las comillas dobles alrededor del `-D` son necesarias:
> ```powershell
> mvn test "-Dcucumber.filter.tags=@login_mainframe"
> ```

### Empaquetar para AWS Device Farm

```bash
mvn clean package -DskipTests
```

Genera `target/zip-with-dependencies.zip` para subir a Device Farm junto al APK.

### En AWS Device Farm

1. Crear un run → subir APK + `zip-with-dependencies.zip`
2. Seleccionar el `testspec.yml` del proyecto
3. En **Advanced settings → Environment variables**:

   | Key | Value | Descripción |
   |---|---|---|
   | `TEST_TAGS` | `@login_mainframe` | Solo login y home |
   | `TEST_TAGS` | `@own_transfer` | Solo transferencias propias |
   | `TEST_TAGS` | `@regression` | Suite completa (default) |

   Si no se define `TEST_TAGS`, corre `@regression` por defecto.

### Reportes

Tras la ejecución se generan en `target/`:

```
target/
├── cucumber-reports/
│   ├── report.html     ← reporte visual
│   └── report.json     ← para integración CI/CD
└── screenshots/        ← capturas de escenarios fallidos
```
