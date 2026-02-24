package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected AndroidDriver driver;
    protected WebDriverWait wait;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    protected void click(WebElement element, String elementName) {
        System.out.println("Haciendo clic en: " + elementName);
        waitForVisibility(element);
        element.click();
    }

    protected void sendKeys(WebElement element, String text, String elementName) {
        System.out.println("Escribiendo '" + text + "' en: " + elementName);
        waitForVisibility(element);
        element.click();
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(text).perform();
    }

    protected void sendPassword(WebElement element, String text, String elementName) {
        System.out.println("Escribiendo '********' en: " + elementName);
        waitForVisibility(element);
        element.click();
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(text).perform();
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected boolean isDisplayed(WebElement element) {
        try { return element.isDisplayed(); } catch (Exception e) { return false; }
    }

    public static String takeScreenshot(AndroidDriver driver, String screenshotName) {
        String dateName = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
        java.io.File source = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";
        try {
            org.apache.commons.io.FileUtils.copyFile(source, new java.io.File(destination));
        } catch (java.io.IOException e) { e.printStackTrace(); }
        return destination;
    }
}
