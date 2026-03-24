package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferReceiptScreen extends BaseScreen {

    // testTag: title_transfer_own_receipt — texto "¡Transferencia exitosa!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"title_transfer_own_receipt\")")
    protected WebElement txtTransferenciaExitosa;

    public OwnTransferReceiptScreen(AndroidDriver driver) { super(driver); }
}
