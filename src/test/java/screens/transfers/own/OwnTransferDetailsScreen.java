package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferDetailsScreen extends BaseScreen {

    // testTag: card_transfer_own_origin
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"card_transfer_own_origin\")")
    protected WebElement originAccountCard;

    // testTag: field_transfer_own_amount
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"field_transfer_own_amount\")")
    protected WebElement amountField;

    // testTag: btn_transfer_own_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_own_continue\")")
    protected WebElement btnContinue;

    public OwnTransferDetailsScreen(AndroidDriver driver) { super(driver); }
}
