package screens.transfers.losandes;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LosAndesDetailsScreen extends BaseScreen {

    // testTag: field_transfer_third_amount
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"txt_transfer_thirdcla_details_monto\")")
    protected WebElement amountField;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"txt_transfer_thirdcla_details_motivo\")")
    protected WebElement obsField;

    // testTag: btn_transfer_third_details_continue
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_thirdcla_details_continuar\")")
    protected WebElement btnContinue;

    public LosAndesDetailsScreen(AndroidDriver driver) { super(driver); }
}
