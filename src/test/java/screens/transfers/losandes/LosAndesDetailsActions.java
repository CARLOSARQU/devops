package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LosAndesDetailsActions extends LosAndesDetailsScreen {

    public LosAndesDetailsActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de detalles cargó (max 15 seg — espera respuesta API)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(amountField));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de detalles no cargó a tiempo");
            return false;
        }
    }

    public LosAndesTransferSummaryActions enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        scrollToElement("third_party_details_continue_button");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new LosAndesTransferSummaryActions(driver);
    }
}
