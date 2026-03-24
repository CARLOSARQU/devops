package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditMenuScreen extends BaseScreen {

    // testTag: credit_menu_pago_cuota
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_menu_pago_cuota\")")
    protected WebElement btnPagarCuota;

    public CreditMenuScreen(AndroidDriver driver) { super(driver); }
}
