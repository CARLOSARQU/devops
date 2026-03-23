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
    public void setUp(Scenario scenario) {
        log.info("--------------------------------------------------------");
        ScenarioContext.reset();
        boolean requiresFreshStart = scenario.getSourceTagNames().contains("@login_test");
        if (!ScenarioContext.isLoggedIn() || requiresFreshStart) {
            if (requiresFreshStart) {
                log.info("[SETUP] Escenario de login — reinicio completo forzado.");
                ScenarioContext.setLoggedIn(false);
            } else {
                log.info("[SETUP] Sin sesión activa — reinicio completo de la app.");
            }
            DriverManager.resetApp();
        } else {
            log.info("[SETUP] Sesión activa — reiniciando app sin borrar datos.");
            DriverManager.restartApp();
        }
        log.info("[SETUP] App lista.");
        log.info("--------------------------------------------------------");
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("[TEARDOWN] Escenario fallido: {} — guardando screenshot", scenario.getName());
            if (DriverManager.getDriver() != null) {
                BasePage.takeScreenshot(DriverManager.getDriver(), scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"));
            }
            log.warn("[TEARDOWN] Marcando sesión como inválida — próximo escenario arrancará desde cero.");
            ScenarioContext.setLoggedIn(false);
        } else {
            log.info("[TEARDOWN] Escenario '{}' finalizado correctamente.", scenario.getName());
        }
    }
}
