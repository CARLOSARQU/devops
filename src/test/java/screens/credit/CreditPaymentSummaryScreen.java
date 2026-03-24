package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditPaymentSummaryScreen extends BaseScreen {

    // testTag: btn_credit_summary_pay
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_credit_summary_pay\")")
    protected WebElement btnPagarCuota;

    public CreditPaymentSummaryScreen(AndroidDriver driver) { super(driver); }
}
