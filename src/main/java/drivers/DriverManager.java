package drivers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import utils.ConfigReader;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.File;

import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverManager {
    protected static final Logger log = LogManager.getLogger(DriverManager.class);

    private static ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();
    // NUEVO: Variable para controlar el servidor de Appium por cada hilo
    private static ThreadLocal<AppiumDriverLocalService> appiumService = new ThreadLocal<>();

    public static AndroidDriver getDriver() {
        if (driver.get() == null) {
            setupDriver();
        }
        return driver.get();
    }

    public static void startAppiumServer() {
        log.info("Iniciando servidor Appium internamente...");

        // Creamos la ruta donde se guardará el log (en la carpeta target que se limpia con Maven)
        File appiumLogFile = new File("target/appium-server.log");

        AppiumDriverLocalService service = new AppiumServiceBuilder()
                .usingAnyFreePort()
                // 1. Le decimos que solo imprima errores graves en la consola (silencia la "basura")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                // 2. Le decimos que guarde TODO el detalle en un archivo de texto
                .withLogFile(appiumLogFile)
                .build();

        service.start();
        appiumService.set(service);
        log.info("Servidor Appium corriendo silenciosamente en: " + service.getUrl());
        log.info("Los logs de Appium se están guardando en: " + appiumLogFile.getAbsolutePath());
    }

    private static void setupDriver() {
        String platformName = System.getProperty("platform.name", ConfigReader.getProperty("platform.name"));
        String deviceName = System.getProperty("device.name", ConfigReader.getProperty("device.name"));
        String apkPath = System.getProperty("app.apk.path");

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
            throw new RuntimeException("Error conectando con Appium: " + e.getMessage());
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