package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditPaymentReceiptScreen extends BaseScreen {

    // testTag: credit_receipt_success_title — texto "¡Pago exitoso!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_receipt_success_title\")")
    protected WebElement txtPagoExitoso;

    // testTag: credit_receipt_go_home_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_receipt_go_home_button\")")
    protected WebElement btnIrAlInicio;

    public CreditPaymentReceiptScreen(AndroidDriver driver) { super(driver); }
}
