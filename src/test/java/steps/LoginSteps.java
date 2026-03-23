package steps;

import drivers.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;

public class LoginSteps {
    private static final Logger log = LogManager.getLogger(LoginSteps.class);
    private LoginPage loginPage;

    @When("ingreso el usuario {string} y la clave {string}")
    public void ingresoElUsuarioYLaClave(String usuario, String clave) {
        log.info("Paso: ingreso usuario {} y clave ********", usuario);
        WelcomePage welcomePage = new WelcomePage(DriverManager.getDriver());
        loginPage = welcomePage.irALogin();
        loginPage.login(usuario, clave);
    }

    @Then("el resultado debería ser {string}")
    public void elResultadoDeberiaSerExitoso(String esperado) {
        if (esperado.equals("exitoso")) {
            log.info("Paso: verificando login exitoso");
            HomePage home = new HomePage(DriverManager.getDriver());
            Assert.assertTrue(home.isHomePageDisplayed(), "Home no visible tras login exitoso");
            log.info("RESULTADO: Login Exitoso confirmado.");
        } else {
            log.info("Paso: verificando error de login");
            Assert.assertTrue(loginPage.isErrorModalDisplayed(), "Modal de error no apareció");
            loginPage.cerrarModalError();
            log.info("RESULTADO: Validación de error por credenciales correcta.");
        }
    }
}
