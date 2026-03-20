package steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class HomeSteps {
    private static final Logger log = LogManager.getLogger(HomeSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    @Entonces("la sección Mis productos es visible")
    public void laSectionMisProductosEsVisible() {
        log.info("Paso: verificando que la sección Mis productos es visible");
        Assert.assertTrue(context.homePage.isHomePageDisplayed(),
                "La sección 'Mis productos' no es visible en Home");
        log.info("RESULTADO: Home Page verificada correctamente.");
    }

    @Entonces("los shortcuts de Transferir, Transferencia Celular, Pagar Cuota y Abrir Cuenta Digital son visibles")
    public void losShortcutsSonVisibles() {
        log.info("Paso: verificando shortcuts visibles");
        Assert.assertTrue(context.homePage.isShortcutTransferirVisible(),          "Shortcut 'Transferir' no visible");
        Assert.assertTrue(context.homePage.isShortcutTransferenciaCelularVisible(), "Shortcut 'Transferencia Celular' no visible");
        Assert.assertTrue(context.homePage.isShortcutPagarCuotaVisible(),           "Shortcut 'Pagar Cuota' no visible");
        Assert.assertTrue(context.homePage.isShortcutAbrirCuentaDigitalVisible(),   "Shortcut 'Abrir Cuenta Digital' no visible");
        log.info("RESULTADO: Todos los shortcuts verificados correctamente.");
    }

    @Cuando("alterno el toggle de saldo")
    public void alternoElToggleDeSaldo() {
        log.info("Paso: alternando toggle de saldo");
        context.homePage.clickToggleBalance();
    }

    @Entonces("la pantalla Home sigue mostrándose correctamente")
    public void laPantallaHomeSigueMostrandose() {
        log.info("Paso: verificando que Home sigue mostrándose");
        Assert.assertTrue(context.homePage.isHomePageDisplayed(),
                "Home dejó de mostrarse correctamente después de alternar el saldo");
        log.info("RESULTADO: Toggle de balance funciona correctamente.");
    }
}
