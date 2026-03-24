package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesDetailsScreen extends BaseScreen {

    // testTag: third_party_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_amount_field\")")
    protected WebElement amountField;

    // testTag: third_party_details_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_details_continue_button\")")
    protected WebElement btnContinue;

    public LosAndesDetailsScreen(AndroidDriver driver) { super(driver); }
}
