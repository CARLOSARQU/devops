package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CreditPaymentReceiptScreen extends BaseScreen {

    // testTag: title_credit_receipt_success — texto "¡Pago exitoso!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"title_credit_receipt_success\")")
    protected WebElement txtPagoExitoso;

    // testTag: btn_credit_receipt_go_home
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_credit_receipt_go_home\")")
    protected WebElement btnIrAlInicio;

    public CreditPaymentReceiptScreen(AndroidDriver driver) { super(driver); }
}
