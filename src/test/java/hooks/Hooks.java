package hooks;

import drivers.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.BasePage;
import steps.ScenarioContext;

public class Hooks {
    private static final Logger log = LogManager.getLogger(Hooks.class);

    @BeforeAll
    public static void startSuite() {
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

    @AfterAll
    public static void stopSuite() {
        log.info("========================================================");
        log.info("[SUITE] Cerrando driver y apagando Appium...");
        log.info("========================================================");
        DriverManager.quitDriver();
        DriverManager.stopAppiumServer();
        log.info("[SUITE] Suite finalizada correctamente.");
        log.info("========================================================");
    }

    @Before
    public void setUp() {
        log.info("--------------------------------------------------------");
        log.info("[SETUP] Reseteando App para nuevo Escenario...");
        log.info("--------------------------------------------------------");
        ScenarioContext.reset();
        DriverManager.resetApp();
        log.info("[SETUP] App lista.");
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("[TEARDOWN] Escenario fallido: {} — guardando screenshot", scenario.getName());
            if (DriverManager.getDriver() != null) {
                BasePage.takeScreenshot(DriverManager.getDriver(), scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"));
            }
        } else {
            log.info("[TEARDOWN] Escenario '{}' finalizado correctamente.", scenario.getName());
        }
    }
}
