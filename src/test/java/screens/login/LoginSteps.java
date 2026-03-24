package screens.login;

import drivers.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import screens.home.HomeActions;
import screens.onboarding.WelcomeActions;

public class LoginSteps {
    private static final Logger log = LogManager.getLogger(LoginSteps.class);
    private LoginActions loginActions;

    @When("ingreso el usuario {string} y la clave {string}")
    public void ingresoElUsuarioYLaClave(String usuario, String clave) {
        log.info("Paso: ingreso usuario {} y clave ********", usuario);
        WelcomeActions welcomeActions = new WelcomeActions(DriverManager.getDriver());
        loginActions = welcomeActions.irALogin();
        loginActions.login(usuario, clave);
    }

    @Then("el resultado debería ser {string}")
    public void elResultadoDeberiaSerExitoso(String esperado) {
        if (esperado.equals("exitoso")) {
            log.info("Paso: verificando login exitoso");
            HomeActions home = new HomeActions(DriverManager.getDriver());
            Assert.assertTrue(home.isHomePageDisplayed(), "Home no visible tras login exitoso");
            log.info("RESULTADO: Login Exitoso confirmado.");
        } else {
            log.info("Paso: verificando error de login");
            Assert.assertTrue(loginActions.isErrorModalDisplayed(), "Modal de error no apareció");
            loginActions.cerrarModalError();
            log.info("RESULTADO: Validación de error por credenciales correcta.");
        }
    }
}
