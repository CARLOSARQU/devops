package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditMenuScreen extends BaseScreen {

    // testTag: btn_credit_menu_pay_quota
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_credit_menu_pay_quota\")")
    protected WebElement btnPagarCuota;

    public CreditMenuScreen(AndroidDriver driver) { super(driver); }
}
