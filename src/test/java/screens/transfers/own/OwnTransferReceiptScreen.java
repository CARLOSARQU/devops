package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferReceiptScreen extends BaseScreen {

    // testTag: transfer_receipt_title — texto "¡Transferencia exitosa!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_receipt_title\")")
    protected WebElement txtTransferenciaExitosa;

    public OwnTransferReceiptScreen(AndroidDriver driver) { super(driver); }
}
