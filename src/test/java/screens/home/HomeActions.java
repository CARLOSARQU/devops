package screens.home;

import io.appium.java_client.android.AndroidDriver;
import screens.operations.OperationMenuActions;

public class HomeActions extends HomeScreen {

    public HomeActions(AndroidDriver driver) {
        super(driver);
        gestionarModalAviso();
    }

    private void gestionarModalAviso() {
        log.info("--- Verificando posible modal de aviso ---");
        try {
            waitForVisibility(btnEntendidoModal, 40);
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
            waitForVisibility(txtMisProductos, 15);
            return true;
        } catch (Exception e) {
            log.warn("'Mis productos' no es visible en el tiempo esperado");
            return false;
        }
    }

    public HomeActions clickToggleBalance() {
        log.info("Alternando visibilidad de saldo");
        click(btnToggleBalance, "Toggle Balance");
        return this;
    }

    public boolean isNavBarDisplayed() {
        return isDisplayed(btnNavOperaciones);
    }

    public OperationMenuActions irAOperaciones() {
        log.info("Navegando a Operaciones via bottom nav");
        click(btnNavOperaciones, "Tab Operaciones");
        return new OperationMenuActions(driver);
    }

    // TODO: reemplazar por testTag cuando el design system exponga btn_nav_campaigns
    public HomeActions irACampanas() {
        log.info("Navegando a Campañas via bottom nav");
        click(btnNavCampanas, "Tab Campañas");
        return this;
    }

    // TODO: reemplazar por testTag cuando el design system exponga btn_nav_menu
    public HomeActions irAMenu() {
        log.info("Navegando a Menú via bottom nav");
        click(btnNavMenu, "Tab Menú");
        return this;
    }
}
