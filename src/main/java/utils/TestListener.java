package utils;
import drivers.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import pages.BasePage;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("TEST FALLIDO: " + result.getName());
        if (DriverManager.getDriver() != null) {
            saveScreenshotPNG();
            BasePage.takeScreenshot(DriverManager.getDriver(), result.getName());
        }
    }
    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPNG() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
