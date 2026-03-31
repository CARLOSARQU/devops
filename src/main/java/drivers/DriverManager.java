package drivers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import utils.ConfigReader;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverManager {
    protected static final Logger log = LogManager.getLogger(DriverManager.class);

    private static ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<AppiumDriverLocalService> appiumService = new ThreadLocal<>();

    private static final String BROWSERSTACK_URL = "https://hub-cloud.browserstack.com/wd/hub";

    public static boolean isBrowserStack() {
        return "browserstack".equalsIgnoreCase(ConfigReader.getProperty("execution.mode"));
    }

    public static boolean isDeviceFarm() {
        return System.getenv("DEVICEFARM_DEVICE_UDID") != null;
    }

    public static AndroidDriver getDriver() {
        return driver.get();
    }

    public static void initDriver() {
        if (driver.get() == null) {
            log.info("Creando sesión del driver...");
            if (isBrowserStack()) {
                setupBrowserStackDriver();
            } else if (isDeviceFarm()) {
                setupDeviceFarmDriver();
            } else {
                setupLocalDriver();
            }
            log.info("Sesión del driver lista.");
        }
    }

    public static void startAppiumServer() {
        if (isBrowserStack()) {
            log.info("[BrowserStack] Servidor Appium no requerido — usando servidor remoto de BrowserStack.");
            return;
        }
        if (isDeviceFarm()) {
            log.info("[DeviceFarm] Servidor Appium no requerido — Device Farm lo gestiona automáticamente.");
            return;
        }

        log.info("Iniciando servidor Appium internamente...");
        File appiumLogFile = new File("target/appium-server.log");

        String appiumPath = System.getProperty("appium.executable",
                System.getenv("APPIUM_EXECUTABLE") != null
                        ? System.getenv("APPIUM_EXECUTABLE")
                        : "appium");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .withLogFile(appiumLogFile);

        if (!appiumPath.equals("appium")) {
            builder.withAppiumJS(new File(appiumPath));
        }

        AppiumDriverLocalService service = builder.build();

        service.start();
        appiumService.set(service);
        log.info("Servidor Appium corriendo en: " + service.getUrl());
        log.info("Logs de Appium guardados en: " + appiumLogFile.getAbsolutePath());
    }

    private static void setupLocalDriver() {
        String platformName = System.getProperty("platform.name", ConfigReader.getProperty("platform.name"));
        String deviceName   = System.getProperty("device.name",   ConfigReader.getProperty("device.name"));
        String apkPath      = System.getProperty("app.apk.path");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(platformName)
                .setDeviceName(deviceName)
                .setAutomationName(ConfigReader.getProperty("automation.name"))
                .setNoReset(Boolean.parseBoolean(ConfigReader.getProperty("no.reset")))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(300));

        if (apkPath != null && !apkPath.isEmpty()) {
            options.setApp(apkPath);
            log.info("Jenkins inyectó un APK. Instalando desde: " + apkPath);
        } else {
            options.setAppPackage(ConfigReader.getProperty("app.package"));
            options.setAppActivity(ConfigReader.getProperty("app.activity"));
            log.info("Ejecución local: Usando appPackage y appActivity");
        }

        try {
            URL dynamicUrl = appiumService.get().getUrl();
            AndroidDriver newDriver = new AndroidDriver(dynamicUrl, options);
            newDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            driver.set(newDriver);
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Appium local: " + e.getMessage());
        }
    }

    private static void setupDeviceFarmDriver() {
        log.info("[DeviceFarm] Conectando al servidor Appium local de Device Farm...");

        // Appium 3: --default-capabilities fue eliminado. El cliente pasa todas las capabilities.
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(System.getenv("DEVICEFARM_DEVICE_PLATFORM_NAME"))
                .setDeviceName(System.getenv("DEVICEFARM_DEVICE_NAME"))
                .setUdid(System.getenv("DEVICEFARM_DEVICE_UDID"))
                .setPlatformVersion(System.getenv("DEVICEFARM_DEVICE_OS_VERSION"))
                .setApp(System.getenv("DEVICEFARM_APP_PATH"))
                .setAutomationName("UiAutomator2")
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(300));

        String chromedriverDir = System.getenv("DEVICEFARM_CHROMEDRIVER_EXECUTABLE_DIR");
        if (chromedriverDir != null && !chromedriverDir.isEmpty()) {
            options.setCapability("appium:chromedriverExecutableDir", chromedriverDir);
        }

        try {
            URL deviceFarmUrl = new URL("http://0.0.0.0:4723/wd/hub");
            AndroidDriver newDriver = new AndroidDriver(deviceFarmUrl, options);
            newDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            driver.set(newDriver);
            log.info("[DeviceFarm] Conectado. Dispositivo: {} ({})",
                System.getenv("DEVICEFARM_DEVICE_NAME"), System.getenv("DEVICEFARM_DEVICE_UDID"));
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Device Farm: " + e.getMessage());
        }
    }

    private static void setupBrowserStackDriver() {
        log.info("[BrowserStack] Conectando con dispositivo remoto...");

        String username  = ConfigReader.getProperty("browserstack.username");
        String accessKey = ConfigReader.getProperty("browserstack.access.key");
        String appUrl    = ConfigReader.getProperty("browserstack.app.url");
        String device    = ConfigReader.getProperty("browserstack.device");
        String osVersion = ConfigReader.getProperty("browserstack.os.version");

        UiAutomator2Options options = new UiAutomator2Options();
        options.setApp(appUrl);
        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName",    username);
        bstackOptions.put("accessKey",   accessKey);
        bstackOptions.put("deviceName",  device);
        bstackOptions.put("osVersion",   osVersion);
        bstackOptions.put("projectName", "Los Andes QA");
        bstackOptions.put("buildName",   "QA Automation Build");
        bstackOptions.put("sessionName", "Automated Test Session");
        options.setCapability("bstack:options", bstackOptions);

        try {
            AndroidDriver newDriver = new AndroidDriver(new URL(BROWSERSTACK_URL), options);
            newDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            driver.set(newDriver);
            log.info("[BrowserStack] Conectado. Dispositivo: {} Android {}", device, osVersion);
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con BrowserStack: " + e.getMessage());
        }
    }

    public static void resetApp() {
        AndroidDriver currentDriver = getDriver();
        String appPackage = ConfigReader.getProperty("app.package");
        log.info("--- Limpiando y reiniciando app: " + appPackage + " ---");
        currentDriver.terminateApp(appPackage);
        currentDriver.executeScript("mobile: clearApp", java.util.Map.of("appId", appPackage));
        currentDriver.activateApp(appPackage);
        try { Thread.sleep(15000); } catch (InterruptedException ignored) {}
        log.info("--- App reiniciada desde cero ---");
    }

    public static void restartApp() {
        AndroidDriver currentDriver = getDriver();
        String appPackage = ConfigReader.getProperty("app.package");
        log.info("--- Reiniciando app sin borrar datos (sesión preservada): " + appPackage + " ---");
        currentDriver.terminateApp(appPackage);
        currentDriver.activateApp(appPackage);
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        log.info("--- App reiniciada. Token de sesión preservado ---");
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            log.info("Cerrando sesión del driver...");
            driver.get().quit();
            driver.remove();
            log.info("Sesión del dispositivo terminada.");
        } else {
            log.warn("quitDriver() llamado pero no había driver activo.");
        }
    }

    public static void stopAppiumServer() {
        if (isBrowserStack() || isDeviceFarm()) {
            log.info("[{}] No hay servidor local que apagar.", isDeviceFarm() ? "DeviceFarm" : "BrowserStack");
            return;
        }
        if (appiumService.get() != null) {
            log.info("Apagando servidor Appium...");
            appiumService.get().stop();
            appiumService.remove();
            log.info("Servidor Appium apagado correctamente.");
        } else {
            log.warn("stopAppiumServer() llamado pero no había servidor Appium activo.");
        }
    }
}