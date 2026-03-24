package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferDetailsScreen extends BaseScreen {

    // testTag: transfer_origin_account_card
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_origin_account_card\")")
    protected WebElement originAccountCard;

    // testTag: transfer_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_amount_field\")")
    protected WebElement amountField;

    // testTag: transfer_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_continue_button\")")
    protected WebElement btnContinue;

    public OwnTransferDetailsScreen(AndroidDriver driver) { super(driver); }
}
