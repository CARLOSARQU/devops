package tests;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.ITestResult;
import pages.BasePage;

public class BaseTest {
    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        log.info("********************************************************");
        log.info("[SETUP] Inicializando Driver y limpiando App para nuevo Test");
        log.info("********************************************************");
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
        log.info("[TEARDOWN] Cerrando Driver y liberando recursos");
        DriverManager.quitDriver();
    }
}
