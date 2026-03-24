package screens.home;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import screens.operations.OperationMenuActions;
import java.time.Duration;

public class HomeActions extends HomeScreen {

    public HomeActions(AndroidDriver driver) {
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

    public HomeActions clickToggleBalance() {
        log.info("Alternando visibilidad de saldo");
        click(btnToggleBalance, "Toggle Balance");
        return this;
    }

    public OperationMenuActions irAOperaciones() {
        log.info("Navegando a Operaciones via bottom nav");
        clickByText("Operaciones", "Tab Operaciones");
        return new OperationMenuActions(driver);
    }
}
