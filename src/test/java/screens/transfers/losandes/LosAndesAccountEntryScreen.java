package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesAccountEntryScreen extends BaseScreen {

    // testTag: third_party_account_number_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_number_field\")")
    protected WebElement accountNumberField;

    // testTag: third_party_account_entry_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_entry_continue_button\")")
    protected WebElement btnContinue;

    public LosAndesAccountEntryScreen(AndroidDriver driver) { super(driver); }
}
