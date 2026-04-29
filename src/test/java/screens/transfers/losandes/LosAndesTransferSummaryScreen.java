package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesTransferSummaryScreen extends BaseScreen {

    // testTag: btn_transfer_third_summary_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_thirdcla_summary_continuar\")")
    protected WebElement btnContinue;

    public LosAndesTransferSummaryScreen(AndroidDriver driver) { super(driver); }
}
