package tests;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.BasePage;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;
import utils.ConfigReader;

public class BaseTest {
    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void startSuite() {
        log.info("========================================================");
        log.info("[SUITE] Iniciando servidor Appium...");
        log.info("========================================================");
        DriverManager.startAppiumServer();
        log.info("[SUITE] Servidor Appium listo.");
        log.info("[SUITE] Creando sesión del driver (una sola vez)...");
        DriverManager.initDriver();
        log.info("[SUITE] Driver listo. Comenzando tests.");
        log.info("========================================================");
    }

    @AfterSuite
    public void stopSuite() {
        log.info("========================================================");
        log.info("[SUITE] Cerrando driver y apagando Appium...");
        log.info("========================================================");
        DriverManager.quitDriver();
        DriverManager.stopAppiumServer();
        log.info("[SUITE] Suite finalizada correctamente.");
        log.info("========================================================");
    }

    @BeforeMethod
    public void setUp() {
        log.info("--------------------------------------------------------");
        log.info("[SETUP] Reseteando App para nuevo Test...");
        log.info("--------------------------------------------------------");
        DriverManager.resetApp();
        log.info("[SETUP] App lista.");
    }

    protected HomePage loginAndGoHome() {
        String dni      = ConfigReader.getProperty("test.dni");
        String password = ConfigReader.getProperty("test.password");
        WelcomePage welcomePage = new WelcomePage(DriverManager.getDriver());
        LoginPage loginPage     = welcomePage.irALogin();
        return loginPage.loginSuccessful(dni, password);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            if (DriverManager.getDriver() != null) {
                log.error("[TEARDOWN] Test fallido: {} — guardando screenshot", result.getName());
                BasePage.takeScreenshot(DriverManager.getDriver(), result.getName());
            }
        } else {
            log.info("[TEARDOWN] Test '{}' finalizado correctamente.", result.getName());
        }
    }
}
