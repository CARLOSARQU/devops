package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesTransferReceiptScreen extends BaseScreen {

    // testTag: title_transfer_third_receipt — texto "Transferencia exitosa"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"title_transfer_third_receipt\")")
    protected WebElement txtTransferenciaExitosa;

    public LosAndesTransferReceiptScreen(AndroidDriver driver) { super(driver); }
}
