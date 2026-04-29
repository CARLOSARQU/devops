package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesAccountEntryScreen extends BaseScreen {

    // testTag: field_transfer_third_account_number
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"txt_transfer_thirdcla_account_cci\")")
    protected WebElement accountNumberField;

    // testTag: btn_transfer_third_entry_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_thirdcla_account_continue\")")
    protected WebElement btnContinue;

    public LosAndesAccountEntryScreen(AndroidDriver driver) { super(driver); }
}
