package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesOtpScreen extends BaseScreen {

    // Dialog OTP del design system AAR — sin testTag, se localiza por texto
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Enviar\")")
    protected WebElement btnEnviar;

    public LosAndesOtpScreen(AndroidDriver driver) { super(driver); }
}
