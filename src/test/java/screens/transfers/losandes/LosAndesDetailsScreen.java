package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesDetailsScreen extends BaseScreen {

    // testTag: field_transfer_third_amount
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"field_transfer_third_amount\")")
    protected WebElement amountField;

    // testTag: btn_transfer_third_details_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_third_details_continue\")")
    protected WebElement btnContinue;

    public LosAndesDetailsScreen(AndroidDriver driver) { super(driver); }
}
