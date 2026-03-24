package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesTransferReceiptScreen extends BaseScreen {

    // testTag: third_party_receipt_title — texto "Transferencia exitosa"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_receipt_title\")")
    protected WebElement txtTransferenciaExitosa;

    public LosAndesTransferReceiptScreen(AndroidDriver driver) { super(driver); }
}
