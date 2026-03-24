package screens.operations;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class OperationMenuScreen extends BaseScreen {

    // testTag: btn_operations_transfers
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_transfers\")")
    protected WebElement btnTransferencias;

    // testTag: btn_operations_pay_credit
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_operations_pay_credit\")")
    protected WebElement btnPagarCredito;

    public OperationMenuScreen(AndroidDriver driver) { super(driver); }
}
