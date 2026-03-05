package tests.home;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;
import tests.BaseTest;
import utils.ConfigReader;

public class HomeTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(HomeTest.class);
    private HomePage homePage;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        String dni      = ConfigReader.getProperty("test.dni");
        String password = ConfigReader.getProperty("test.password");

        WelcomePage welcomePage = new WelcomePage(DriverManager.getDriver());
        LoginPage loginPage     = welcomePage.irALogin();
        homePage                = loginPage.loginSuccessful(dni, password);
    }

    @Test(description = "La pantalla Home carga correctamente tras un login exitoso")
    public void testHomeCargaCorrectamente() {
        log.info("========== TEST: Home carga correctamente ==========");
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "La sección 'Mis productos' no es visible en Home");
        log.info("RESULTADO: Home Page verificada correctamente.");
    }

    @Test(description = "Los accesos rápidos principales están visibles en Home")
    public void testShortcutsVisibles() {
        log.info("========== TEST: Shortcuts visibles ==========");
        Assert.assertTrue(homePage.isShortcutTransferirVisible(),
                "El shortcut 'Transferir' no es visible");
        Assert.assertTrue(homePage.isShortcutTransferenciaCelularVisible(),
                "El shortcut 'Transferencia Celular' no es visible");
        Assert.assertTrue(homePage.isShortcutPagarCuotaVisible(),
                "El shortcut 'Pagar Cuota' no es visible");
        Assert.assertTrue(homePage.isShortcutAbrirCuentaDigitalVisible(),
                "El shortcut 'Abrir Cuenta Digital' no es visible");
        log.info("RESULTADO: Todos los shortcuts verificados correctamente.");
    }

    @Test(description = "El toggle de saldo alterna sin romper la pantalla")
    public void testToggleBalanceFunciona() {
        log.info("========== TEST: Toggle de balance ==========");
        homePage.clickToggleBalance();
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Home dejó de mostrarse correctamente después de alternar el saldo");
        log.info("RESULTADO: Toggle de balance funciona correctamente.");
    }
}
