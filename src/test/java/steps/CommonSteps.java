package steps;

import drivers.DriverManager;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;
import pages.operations.OperationMenuPage;
import pages.operations.TransferMenuPage;
import utils.ConfigReader;

public class CommonSteps {
    private static final Logger log = LogManager.getLogger(CommonSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    @Dado("estoy en la pantalla de bienvenida")
    public void estoyEnLaPantallaDeBienvenida() {
        log.info("Paso: estoy en la pantalla de bienvenida");
    }

    @Dado("el usuario ha iniciado sesión")
    public void elUsuarioHaIniciadoSesion() {
        if (ScenarioContext.isLoggedIn()) {
            log.info("Paso: sesión activa — reutilizando. Inicializando HomePage.");
            context.homePage = new HomePage(DriverManager.getDriver());
            return;
        }
        log.info("Paso: el usuario ha iniciado sesión");
        String dni      = ConfigReader.getProperty("test.dni");
        String password = ConfigReader.getProperty("test.password");
        WelcomePage welcomePage = new WelcomePage(DriverManager.getDriver());
        LoginPage loginPage     = welcomePage.irALogin();
        context.homePage = loginPage.loginSuccessful(dni, password);
        ScenarioContext.setLoggedIn(true);
        log.info("Usuario autenticado correctamente.");
    }

    @Cuando("navego a Operaciones")
    public void navegoAOperaciones() {
        log.info("Paso: navego a Operaciones");
        context.homePage.irAOperaciones();
        context.operationMenu = new OperationMenuPage(DriverManager.getDriver());
    }

    @Y("selecciono Transferencias")
    public void seleccionoTransferencias() {
        log.info("Paso: selecciono Transferencias");
        context.transferMenu = context.operationMenu.clickTransferencias();
    }
}
