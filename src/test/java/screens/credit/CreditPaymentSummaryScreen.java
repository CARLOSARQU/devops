package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditPaymentSummaryScreen extends BaseScreen {

    // testTag: credit_summary_pay_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_summary_pay_button\")")
    protected WebElement btnPagarCuota;

    public CreditPaymentSummaryScreen(AndroidDriver driver) { super(driver); }
}
