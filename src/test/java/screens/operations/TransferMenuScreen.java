package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class TransferMenuScreen extends BaseScreen {

    // testTag: btn_operations_transfer_cuentas
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_transfer_cuentas\")")
    protected WebElement btnEntreMisCuentas;

    // testTag: btn_operations_transfer_cuentas_terceros
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_transfer_cuentas_terceros\")")
    protected WebElement btnOtrasCuentasAndes;

    // testTag: btn_operations_transfer_otras_entidades
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_transfer_otras_entidades\")")
    protected WebElement btnOtrasEntidades;

    // testTag: btn_operations_transfer_celular
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_transfer_celular\")")
    protected WebElement btnCelular;


    public TransferMenuScreen(AndroidDriver driver) { super(driver); }
}
