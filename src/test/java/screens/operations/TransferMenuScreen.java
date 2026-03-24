package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class TransferMenuScreen extends BaseScreen {

    // testTag: btn_transfer_menu_own
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_menu_own\")")
    protected WebElement btnEntreMisCuentas;

    // testTag: btn_transfer_menu_third_party
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_transfer_menu_third_party\")")
    protected WebElement btnOtrasCuentasAndes;

    public TransferMenuScreen(AndroidDriver driver) { super(driver); }
}
