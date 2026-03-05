package pages.home;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    // ── Modal de aviso ──────────────────────────────────────────────────────
    // NOTA: usa .semantics { testTagsAsResourceId = true } en HomeScreen.kt
    // porque los Dialog de Compose no heredan testTagsAsResourceId del Scaffold
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_popup_understood_button\")")
    private WebElement btnEntendidoModal;

    // ── Sección Productos ────────────────────────────────────────────────────
    // testTag: home_products_title
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_products_title\")")
    private WebElement txtMisProductos;

    // testTag: home_toggle_balance
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_toggle_balance\")")
    private WebElement btnToggleBalance;

    // ── Accesos rápidos (off-screen al cargar — requieren scroll) ────────────
    // testTag: home_shortcut_transfer_money
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_transfer_money\")")
    private WebElement btnShortcutTransferir;

    // testTag: home_shortcut_mobile_transfer
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_mobile_transfer\")")
    private WebElement btnShortcutTransferenciaCelular;

    // testTag: home_shortcut_pay_credit
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_pay_credit\")")
    private WebElement btnShortcutPagarCuota;

    // testTag: home_shortcut_open_digital_account
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"home_shortcut_open_digital_account\")")
    private WebElement btnShortcutAbrirCuentaDigital;

    // ────────────────────────────────────────────────────────────────────────

    public HomePage(AndroidDriver driver) {
        super(driver);
        gestionarModalAviso();
    }

    private void gestionarModalAviso() {
        log.info("--- Verificando posible modal de aviso ---");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(ExpectedConditions.visibilityOf(btnEntendidoModal));
            log.info("Modal de aviso detectado. Procediendo a cerrar...");
            btnEntendidoModal.click();
            log.info("Modal cerrado satisfactoriamente.");
        } catch (Exception e) {
            log.info("No se presentó el modal de aviso, el flujo continúa.");
        }
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    public boolean isHomePageDisplayed() {
        log.info("Validando si la Home Page se muestra correctamente");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(txtMisProductos));
            return true;
        } catch (Exception e) {
            log.warn("'Mis productos' no es visible en el tiempo esperado");
            return false;
        }
    }

    // Los shortcuts están fuera del viewport inicial — se hace scroll antes de verificar
    public boolean isShortcutTransferirVisible() {
        scrollToElement("home_shortcut_transfer_money");
        return isDisplayed(btnShortcutTransferir);
    }

    public boolean isShortcutTransferenciaCelularVisible() {
        scrollToElement("home_shortcut_mobile_transfer");
        return isDisplayed(btnShortcutTransferenciaCelular);
    }

    public boolean isShortcutPagarCuotaVisible() {
        scrollToElement("home_shortcut_pay_credit");
        return isDisplayed(btnShortcutPagarCuota);
    }

    public boolean isShortcutAbrirCuentaDigitalVisible() {
        scrollToElement("home_shortcut_open_digital_account");
        return isDisplayed(btnShortcutAbrirCuentaDigital);
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    public HomePage clickToggleBalance() {
        log.info("Alternando visibilidad de saldo");
        click(btnToggleBalance, "Toggle Balance");
        return this;
    }

    // Navega a Operaciones via bottom nav (no tiene testTag — usa contentDescription nativa)
    public void irAOperaciones() {
        log.info("Navegando a Operaciones via bottom nav");
        clickByText("Operaciones", "Tab Operaciones");
    }
}
