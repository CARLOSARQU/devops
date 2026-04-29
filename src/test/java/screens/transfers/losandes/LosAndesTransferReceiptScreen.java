package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesTransferReceiptScreen extends BaseScreen {

    // El equipo de frontend no implementó el testTag — se localiza por texto visible
    @AndroidFindBy(uiAutomator = "new UiSelector().textContains(\"Transferencia exitosa\")")
    protected WebElement txtTransferenciaExitosa;

    // testTag: btn_transfer_thirdcla_receipt_realizar
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_thirdcla_receipt_realizar\")")
    protected WebElement btnRealizarOtraOperacion;

    // testTag: btn_transfer_thirdcla_receipt_compartir (o similar — ajustar con Appium Inspector)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_thirdcla_receipt_compartir\")")
    protected WebElement btnCompartir;

    public LosAndesTransferReceiptScreen(AndroidDriver driver) { super(driver); }
}
