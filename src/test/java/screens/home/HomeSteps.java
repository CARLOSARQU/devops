package screens.home;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import screens.ScenarioContext;

public class HomeSteps {
    private static final Logger log = LogManager.getLogger(HomeSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    @Then("la sección Mis productos es visible")
    public void laSectionMisProductosEsVisible() {
        log.info("Paso: verificando que la sección Mis productos es visible");
        Assert.assertTrue(context.homePage.isHomePageDisplayed(),
                "La sección 'Mis productos' no es visible en Home");
        log.info("RESULTADO: Home Page verificada correctamente.");
    }

    @Then("la barra de navegación inferior es visible")
    public void laBarraDeNavegacionEsVisible() {
        log.info("Paso: verificando que la barra de navegación inferior es visible");
        Assert.assertTrue(context.homePage.isNavBarDisplayed(),
                "La barra de navegación inferior no es visible en Home");
        log.info("RESULTADO: Barra de navegación verificada correctamente.");
    }

    @When("alterno el toggle de saldo")
    public void alternoElToggleDeSaldo() {
        log.info("Paso: alternando toggle de saldo");
        context.homePage.clickToggleBalance();
    }

    @Then("la pantalla Home sigue mostrándose correctamente")
    public void laPantallaHomeSigueMostrandose() {
        log.info("Paso: verificando que Home sigue mostrándose");
        Assert.assertTrue(context.homePage.isHomePageDisplayed(),
                "Home dejó de mostrarse correctamente después de alternar el saldo");
        log.info("RESULTADO: Toggle de balance funciona correctamente.");
    }
}
