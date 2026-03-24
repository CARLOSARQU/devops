package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferSummaryScreen extends BaseScreen {

    // testTag: transfer_summary_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_summary_continue_button\")")
    protected WebElement btnContinue;

    public OwnTransferSummaryScreen(AndroidDriver driver) { super(driver); }
}
