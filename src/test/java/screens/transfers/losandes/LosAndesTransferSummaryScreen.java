package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesTransferSummaryScreen extends BaseScreen {

    // testTag: third_party_summary_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_summary_continue_button\")")
    protected WebElement btnContinue;

    public LosAndesTransferSummaryScreen(AndroidDriver driver) { super(driver); }
}
