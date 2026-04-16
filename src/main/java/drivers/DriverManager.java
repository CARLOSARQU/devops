package drivers;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConfigReader;

public class DriverManager {
    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();

    public static AndroidDriver getDriver() {
        return driver.get();
    }

    public static void startAppiumServer() {
        if (DriverFactory.isBrowserStack()) {
            log.info("[BrowserStack] Servidor Appium no requerido — usando servidor remoto de BrowserStack.");
            return;
        }
        if (DriverFactory.isDeviceFarm()) {
            log.info("[DeviceFarm] Servidor Appium no requerido — Device Farm lo gestiona automáticamente.");
            return;
        }
        AppiumServerManager.start();
    }

    public static void initDriver() {
        if (driver.get() == null) {
            log.info("Creando sesión del driver...");
            driver.set(DriverFactory.create());
            log.info("Sesión del driver lista.");
        }
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
        if (DriverFactory.isBrowserStack() || DriverFactory.isDeviceFarm()) {
            log.info("[{}] No hay servidor local que apagar.",
                    DriverFactory.isDeviceFarm() ? "DeviceFarm" : "BrowserStack");
            return;
        }
        AppiumServerManager.stop();
    }

    public static void resetApp() {
        AndroidDriver currentDriver = getDriver();
        String appPackage = ConfigReader.getProperty("app.package");
        log.info("--- Limpiando y reiniciando app: {} ---", appPackage);
        currentDriver.terminateApp(appPackage);
        currentDriver.executeScript("mobile: clearApp", java.util.Map.of("appId", appPackage));
        currentDriver.activateApp(appPackage);
        try { Thread.sleep(15000); } catch (InterruptedException ignored) {}
        log.info("--- App reiniciada desde cero ---");
    }

    public static void restartApp() {
        AndroidDriver currentDriver = getDriver();
        String appPackage = ConfigReader.getProperty("app.package");
        log.info("--- Reiniciando app sin borrar datos (sesión preservada): {} ---", appPackage);
        currentDriver.terminateApp(appPackage);
        currentDriver.activateApp(appPackage);
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        log.info("--- App reiniciada. Token de sesión preservado ---");
    }
}
