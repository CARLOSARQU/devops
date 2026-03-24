package screens.credit;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreditSelectionActions extends CreditSelectionScreen {

    public CreditSelectionActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Esperando que carguen los créditos en pantalla (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            AppiumBy.androidUIAutomator(
                                    "new UiSelector().resourceIdMatches(\"btn_credit_selection_.*\")")));
            return true;
        } catch (Exception e) {
            log.warn("No apareció ningún crédito en pantalla en el tiempo esperado");
            return false;
        }
    }

    public CreditMenuActions selectFirstCredit() {
        log.info("Seleccionando el primer crédito disponible");
        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElement(AppiumBy.androidUIAutomator(
                        "new UiSelector().resourceIdMatches(\"btn_credit_selection_.*\")")));
        btn.click();
        return new CreditMenuActions(driver);
    }
}
