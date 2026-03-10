package pages.credit;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class CreditSelectionPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CreditSelectionPage.class);

    // testTag: credit_selection_btn_${loanNumber} — botón "Seleccionar" de cada tarjeta
    // No se conoce el loanNumber en tiempo de test, se selecciona el primero disponible.

    public CreditSelectionPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Esperando que carguen los créditos en pantalla (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            io.appium.java_client.AppiumBy.androidUIAutomator(
                                    "new UiSelector().resourceIdMatches(\"credit_selection_btn_.*\")")));
            return true;
        } catch (Exception e) {
            log.warn("No apareció ningún crédito en pantalla en el tiempo esperado");
            return false;
        }
    }

    public CreditMenuPage selectFirstCredit() {
        log.info("Seleccionando el primer crédito disponible");
        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                        "new UiSelector().resourceIdMatches(\"credit_selection_btn_.*\")")));
        btn.click();
        return new CreditMenuPage(driver);
    }
}
