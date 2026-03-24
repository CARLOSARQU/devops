package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class PayCreditMenuScreen extends BaseScreen {

    // testTag: btn_pay_credit_own
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_pay_credit_own\")")
    protected WebElement btnPagarCreditoPropio;

    public PayCreditMenuScreen(AndroidDriver driver) { super(driver); }
}
