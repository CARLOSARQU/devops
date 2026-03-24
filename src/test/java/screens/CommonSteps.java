package screens;

import drivers.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import screens.home.HomeActions;
import screens.login.LoginActions;
import screens.onboarding.WelcomeActions;
import utils.ConfigReader;

public class CommonSteps {
    private static final Logger log = LogManager.getLogger(CommonSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    @Given("estoy en la pantalla de bienvenida")
    public void estoyEnLaPantallaDeBienvenida() {
        log.info("Paso: estoy en la pantalla de bienvenida");
    }

    @Given("el usuario ha iniciado sesión")
    public void elUsuarioHaIniciadoSesion() {
        if (ScenarioContext.isLoggedIn()) {
            log.info("Paso: sesión activa — reutilizando. Inicializando HomeActions.");
            context.homePage = new HomeActions(DriverManager.getDriver());
            return;
        }
        log.info("Paso: el usuario ha iniciado sesión");
        String dni      = ConfigReader.getProperty("test.dni");
        String password = ConfigReader.getProperty("test.password");
        WelcomeActions welcomeActions = new WelcomeActions(DriverManager.getDriver());
        LoginActions loginActions     = welcomeActions.irALogin();
        context.homePage = loginActions.loginSuccessful(dni, password);
        ScenarioContext.setLoggedIn(true);
        log.info("Usuario autenticado correctamente.");
    }

    @When("navego a Operaciones")
    public void navegoAOperaciones() {
        log.info("Paso: navego a Operaciones");
        context.operationMenu = context.homePage.irAOperaciones();
    }

    @And("selecciono Transferencias")
    public void seleccionoTransferencias() {
        log.info("Paso: selecciono Transferencias");
        context.transferMenu = context.operationMenu.clickTransferencias();
    }
}
