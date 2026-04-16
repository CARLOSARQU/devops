package hooks;

import drivers.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;
import base.BaseScreen;
import screens.ScenarioContext;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

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
            log.error("[TEARDOWN] Escenario fallido: {} — guardando evidencia", scenario.getName());
            if (DriverManager.getDriver() != null) {
                adjuntarErrorDebugSiExiste();
                adjuntarModalErrorSiExiste();
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot - " + scenario.getName(), "image/png", new ByteArrayInputStream(screenshot), "png");
                BaseScreen.takeScreenshot(DriverManager.getDriver(), scenario.getName().replaceAll("[^a-zA-Z0-9]", "_"));
            }
            log.warn("[TEARDOWN] Marcando sesión como inválida — próximo escenario arrancará desde cero.");
            ScenarioContext.setLoggedIn(false);
        } else {
            log.info("[TEARDOWN] Escenario '{}' finalizado correctamente.", scenario.getName());
        }
    }

    private void adjuntarErrorDebugSiExiste() {
        AndroidDriver driver = DriverManager.getDriver();
        try {
            driver.openNotifications();

            // Localizar la notificación (visible aunque esté colapsada)
            WebElement notifElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Response url\")"
                )));

            // Expandir para ver Response code y Response body completos
            expandirNotificacion(driver, notifElement);

            // Relocalizar tras la expansión
            WebElement errorElement = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(d -> d.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Response url\")"
                )));

            // Screenshot con la notificación expandida
            byte[] notifScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Notificación error API", "image/png",
                new ByteArrayInputStream(notifScreenshot), "png");

            // Leer el texto completo del bloque expandido
            String errorText;
            try {
                WebElement parent = errorElement.findElement(By.xpath("./.."));
                List<WebElement> textViews = parent.findElements(
                    By.className("android.widget.TextView")
                );
                if (textViews.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (WebElement tv : textViews) {
                        String line = tv.getText();
                        if (line != null && !line.isEmpty()) sb.append(line).append("\n");
                    }
                    errorText = sb.toString().trim();
                } else {
                    errorText = errorElement.getText();
                }
            } catch (Exception e) {
                errorText = errorElement.getText();
            }

            if (errorText != null && !errorText.isEmpty()) {
                log.warn("[TEARDOWN] Error de API detectado:\n{}", errorText);
                Allure.addAttachment("Error API (detalle)", "text/plain",
                    new ByteArrayInputStream(errorText.getBytes(StandardCharsets.UTF_8)), "txt");
            }

        } catch (Exception ignored) {
            // No había notificación de error API en el drawer
        } finally {
            try {
                driver.pressKey(new KeyEvent(AndroidKey.BACK));
            } catch (Exception ignored) {}
        }
    }

    private void expandirNotificacion(AndroidDriver driver, WebElement referenceElement) {
        try {
            int centerX = referenceElement.getLocation().getX()
                        + referenceElement.getSize().getWidth() / 2;
            int startY  = referenceElement.getLocation().getY();
            int endY    = startY + 150;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 0);
            swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(400),
                PointerInput.Origin.viewport(), centerX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));

            Thread.sleep(600); // espera a que termine la animación de expansión
        } catch (Exception e) {
            log.warn("[TEARDOWN] No se pudo expandir la notificación: {}", e.getMessage());
        }
    }

    private void adjuntarModalErrorSiExiste() {
        try {
            AndroidDriver driver = DriverManager.getDriver();

            WebElement modalElement = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(d -> d.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"Hemos tenido un inconveniente\")"
                )));

            String modalText = modalElement.getText();
            log.warn("[TEARDOWN] Modal de error detectado: {}", modalText);
            Allure.addAttachment(
                "Modal de error",
                "text/plain",
                new ByteArrayInputStream(modalText.getBytes(StandardCharsets.UTF_8)),
                "txt"
            );

        } catch (Exception ignored) {
            // No había modal de error visible — no hay nada que adjuntar
        }
    }
}
