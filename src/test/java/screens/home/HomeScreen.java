package screens.home;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class HomeScreen extends BaseScreen {

    // testTag: btn_home_popup_understood
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Entendido\")")
    protected WebElement btnEntendidoModal;

    // testTag: title_home_products
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"title_home_products\")")
    protected WebElement txtMisProductos;

    // testTag: toggle_home_balance
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"toggle_home_balance\")")
    protected WebElement btnToggleBalance;

    // --- Barra de navegación inferior ---
    // Sin testTag (componente en AAR compilado) — localizados por texto visible
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Operaciones\")")
    protected WebElement btnNavOperaciones;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Campañas\")")
    protected WebElement btnNavCampanas;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Menú\")")
    protected WebElement btnNavMenu;

    public HomeScreen(AndroidDriver driver) { super(driver); }
}
