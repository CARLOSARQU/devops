package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    protected void click(WebElement element, String elementName) {
        log.info("Haciendo clic en: {}", elementName);
        waitForVisibility(element);
        element.click();
    }

    protected void clickIfPresent(WebElement element, String elementName) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.visibilityOf(element)).click();
            log.info("Aceptado: {}", elementName);
        } catch (Exception e) {
            log.info("Omitido (no apareció): {}", elementName);
        }
    }

    protected void sendKeys(WebElement element, String text, String elementName) {
        log.info("Escribiendo '{}' en: {}", text, elementName);
        waitForVisibility(element);
        element.click();
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(text).perform();
    }

    protected void sendPassword(WebElement element, String text, String elementName) {
        log.info("Escribiendo '********' en: {}", elementName);
        waitForVisibility(element);
        element.click();
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(text).perform();
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean isDisplayed(WebElement element) {
        try { 
            boolean displayed = element.isDisplayed();
            if(!displayed) log.warn("Elemento no se muestra en pantalla");
            return displayed;
        } catch (Exception e) { 
            log.error("Error al verificar visibilidad: {}", e.getMessage());
            return false; 
        }
    }

    public static String takeScreenshot(AndroidDriver driver, String screenshotName) {
        String dateName = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
        java.io.File source = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";
        try {
            java.io.File destFile = new java.io.File(destination);
            destFile.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.copyFile(source, destFile);
            log.info("Screenshot guardado en: {}", destination);
        } catch (java.io.IOException e) {
            log.error("Error al guardar screenshot: {}", e.getMessage());
        }
        return destination;
    }
}
