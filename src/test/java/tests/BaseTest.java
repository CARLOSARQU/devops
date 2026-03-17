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

public class BaseTest {
    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void startAppium() {
        log.info("========================================================");
        log.info("[SUITE] Iniciando servidor Appium una sola vez...");
        log.info("========================================================");
        DriverManager.startAppiumServer();
        log.info("[SUITE] Servidor Appium listo.");
    }

    @AfterSuite
    public void stopAppium() {
        log.info("========================================================");
        log.info("[SUITE] Apagando servidor Appium al finalizar la suite...");
        log.info("========================================================");
        DriverManager.stopAppiumServer();
        log.info("[SUITE] Servidor Appium detenido.");
    }

    @BeforeMethod
    public void setUp() {
        log.info("--------------------------------------------------------");
        log.info("[SETUP] Limpiando y reiniciando App para nuevo Test");
        log.info("--------------------------------------------------------");
        DriverManager.resetApp();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            if (DriverManager.getDriver() != null) {
                log.error("Test fallido: {} — guardando screenshot", result.getName());
                BasePage.takeScreenshot(DriverManager.getDriver(), result.getName());
            }
        }
        log.info("[TEARDOWN] Cerrando Driver (Appium sigue corriendo)");
        DriverManager.quitDriver();
    }
}
