package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class TransferMenuScreen extends BaseScreen {

    // testTag: menu_transfer_entre_mis_cuentas
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"menu_transfer_entre_mis_cuentas\")")
    protected WebElement btnEntreMisCuentas;

    // testTag: menu_transfer_otras_cuentas_andes
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"menu_transfer_otras_cuentas_andes\")")
    protected WebElement btnOtrasCuentasAndes;

    public TransferMenuScreen(AndroidDriver driver) { super(driver); }
}
