package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class AccountSelectionScreen extends BaseScreen {

    // testTag: btn_credit_account_confirm_pay — siempre renderizado, se habilita cuando la API carga
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_credit_account_confirm_pay\")")
    protected WebElement btnConfirmarPago;

    public AccountSelectionScreen(AndroidDriver driver) { super(driver); }
}
