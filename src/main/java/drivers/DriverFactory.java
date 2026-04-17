package drivers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ConfigReader;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final String BROWSERSTACK_URL = "https://hub-cloud.browserstack.com/wd/hub";
    private static final int COMMAND_TIMEOUT_SECONDS = 90;

    public static AndroidDriver create() {
        if (isBrowserStack()) {
            return createBrowserStackDriver();
        } else if (isDeviceFarm()) {
            return createDeviceFarmDriver();
        } else {
            return createLocalDriver();
        }
    }

    public static boolean isBrowserStack() {
        return "browserstack".equalsIgnoreCase(ConfigReader.getProperty("execution.mode"));
    }

    public static boolean isDeviceFarm() {
        return System.getenv("DEVICEFARM_DEVICE_UDID") != null;
    }

    private static AndroidDriver createLocalDriver() {
        String platformName = System.getProperty("platform.name", ConfigReader.getProperty("platform.name"));
        String deviceName   = System.getProperty("device.name",   ConfigReader.getProperty("device.name"));
        String apkPath      = System.getProperty("app.apk.path");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(platformName)
                .setDeviceName(deviceName)
                .setAutomationName(ConfigReader.getProperty("automation.name"))
                .setNoReset(Boolean.parseBoolean(ConfigReader.getProperty("no.reset")))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(COMMAND_TIMEOUT_SECONDS));

        if (apkPath != null && !apkPath.isEmpty()) {
            options.setApp(apkPath);
            log.info("Jenkins inyectó un APK. Instalando desde: {}", apkPath);
        } else {
            options.setAppPackage(ConfigReader.getProperty("app.package"));
            options.setAppActivity(ConfigReader.getProperty("app.activity"));
            log.info("Ejecución local: Usando appPackage y appActivity");
        }

        try {
            AndroidDriver driver = new AndroidDriver(AppiumServerManager.getServiceUrl(), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            log.info("Driver local creado. Dispositivo: {}", deviceName);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Appium local: " + e.getMessage());
        }
    }

    private static AndroidDriver createDeviceFarmDriver() {
        log.info("[DeviceFarm] Conectando al servidor Appium local de Device Farm...");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(System.getenv("DEVICEFARM_DEVICE_PLATFORM_NAME"))
                .setDeviceName(System.getenv("DEVICEFARM_DEVICE_NAME"))
                .setUdid(System.getenv("DEVICEFARM_DEVICE_UDID"))
                .setPlatformVersion(System.getenv("DEVICEFARM_DEVICE_OS_VERSION"))
                .setApp(System.getenv("DEVICEFARM_APP_PATH"))
                .setAutomationName("UiAutomator2")
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(COMMAND_TIMEOUT_SECONDS));

        String chromedriverDir = System.getenv("DEVICEFARM_CHROMEDRIVER_EXECUTABLE_DIR");
        if (chromedriverDir != null && !chromedriverDir.isEmpty()) {
            options.setCapability("appium:chromedriverExecutableDir", chromedriverDir);
        }

        try {
            AndroidDriver driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            log.info("[DeviceFarm] Conectado. Dispositivo: {} ({})",
                    System.getenv("DEVICEFARM_DEVICE_NAME"), System.getenv("DEVICEFARM_DEVICE_UDID"));
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Device Farm: " + e.getMessage());
        }
    }

    private static AndroidDriver createBrowserStackDriver() {
        log.info("[BrowserStack] Conectando con dispositivo remoto...");

        String username  = ConfigReader.getProperty("browserstack.username");
        String accessKey = ConfigReader.getProperty("browserstack.access.key");
        String appUrl    = ConfigReader.getProperty("browserstack.app.url");
        String device    = ConfigReader.getProperty("browserstack.device");
        String osVersion = ConfigReader.getProperty("browserstack.os.version");

        UiAutomator2Options options = new UiAutomator2Options();
        options.setApp(appUrl);
        options.setAutoGrantPermissions(true);
        options.setNewCommandTimeout(Duration.ofSeconds(COMMAND_TIMEOUT_SECONDS));

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
            AndroidDriver driver = new AndroidDriver(new URL(BROWSERSTACK_URL), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            log.info("[BrowserStack] Conectado. Dispositivo: {} Android {}", device, osVersion);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Error conectando con BrowserStack: " + e.getMessage());
        }
    }
}
