package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class PayCreditMenuScreen extends BaseScreen {

    // testTag: pay_credit_own_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"pay_credit_own_button\")")
    protected WebElement btnPagarCreditoPropio;

    public PayCreditMenuScreen(AndroidDriver driver) { super(driver); }
}
