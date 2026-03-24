package screens.credit;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreditMenuActions extends CreditMenuScreen {

    public CreditMenuActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que el menú 'Paga tu crédito' cargó (max 10 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(btnPagarCuota));
            return true;
        } catch (Exception e) {
            log.warn("El menú 'Paga tu crédito' no cargó a tiempo");
            return false;
        }
    }

    public AccountSelectionActions clickPagarCuota() {
        log.info("Seleccionando opción 'Pago de cuota'");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(btnPagarCuota));
        try {
            click(btnPagarCuota, "Botón Pago de Cuota");
        } catch (Exception e) {
            log.warn("El clic en el ID falló, reintentando clic en el texto del elemento...");
            WebElement txtFallback = driver.findElement(
                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Pago de cuota\")"));
            click(txtFallback, "Texto Pago de Cuota (Fallback)");
        }
        return new AccountSelectionActions(driver);
    }
}
