package screens.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OwnTransferDetailsActions extends OwnTransferDetailsScreen {

    public OwnTransferDetailsActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de detalles de transferencia cargó");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(originAccountCard));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de detalles no cargó a tiempo");
            return false;
        }
    }

    public OwnTransferSummaryActions enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        scrollToElement("transfer_continue_button");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new OwnTransferSummaryActions(driver);
    }
}
