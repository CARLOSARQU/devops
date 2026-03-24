package screens.home;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class HomeScreen extends BaseScreen {

    // testTag: home_popup_understood_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_popup_understood_button\")")
    protected WebElement btnEntendidoModal;

    // testTag: home_products_title
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_products_title\")")
    protected WebElement txtMisProductos;

    // testTag: home_toggle_balance
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_toggle_balance\")")
    protected WebElement btnToggleBalance;

    // testTag: home_shortcut_transfer_money (off-screen — requiere scroll)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_transfer_money\")")
    protected WebElement btnShortcutTransferir;

    // testTag: home_shortcut_mobile_transfer
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_mobile_transfer\")")
    protected WebElement btnShortcutTransferenciaCelular;

    // testTag: home_shortcut_pay_credit
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_pay_credit\")")
    protected WebElement btnShortcutPagarCuota;

    // testTag: home_shortcut_open_digital_account
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_open_digital_account\")")
    protected WebElement btnShortcutAbrirCuentaDigital;

    public HomeScreen(AndroidDriver driver) { super(driver); }
}
