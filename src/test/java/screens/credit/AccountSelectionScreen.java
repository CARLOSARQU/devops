package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class AccountSelectionScreen extends BaseScreen {

    // testTag: account_selection_confirm_pay_button — siempre renderizado, se habilita cuando la API carga
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"account_selection_confirm_pay_button\")")
    protected WebElement btnConfirmarPago;

    public AccountSelectionScreen(AndroidDriver driver) { super(driver); }
}
