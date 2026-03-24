package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OperationMenuScreen extends BaseScreen {

    // testTag: operations_btn_transferencias
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"operations_btn_transferencias\")")
    protected WebElement btnTransferencias;

    // testTag: operations_btn_pagar_credito
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"operations_btn_pagar_credito\")")
    protected WebElement btnPagarCredito;

    public OperationMenuScreen(AndroidDriver driver) { super(driver); }
}
