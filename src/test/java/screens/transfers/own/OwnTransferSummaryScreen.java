package screens.transfers.own;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OwnTransferSummaryScreen extends BaseScreen {

    // testTag: btn_transfer_own_summary_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_own_summary_continue\")")
    protected WebElement btnContinue;

    public OwnTransferSummaryScreen(AndroidDriver driver) { super(driver); }
}
