package drivers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import utils.ConfigReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();
    public static AndroidDriver getDriver() {
        if (driver.get() == null) setupDriver();
        return driver.get();
    }
    private static void setupDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(ConfigReader.getProperty("platform.name"))
                .setDeviceName(ConfigReader.getProperty("device.name"))
                .setAppPackage(ConfigReader.getProperty("app.package"))
                .setAppActivity(ConfigReader.getProperty("app.activity"))
                .setAutomationName(ConfigReader.getProperty("automation.name"))
                .setNoReset(Boolean.parseBoolean(ConfigReader.getProperty("no.reset")))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(300));
        try {
            AndroidDriver newDriver = new AndroidDriver(new URL(ConfigReader.getProperty("appium.url")), options);
            newDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            driver.set(newDriver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL de Appium inválida");
        }
    }
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
