package screens.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OwnTransferSummaryActions extends OwnTransferSummaryScreen {

    public OwnTransferSummaryActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de resumen cargó (max 20 seg — espera respuesta API)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(btnContinue));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de resumen no cargó a tiempo");
            return false;
        }
    }

    public OwnTransferReceiptActions clickContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        click(btnContinue, "Botón Continuar (Resumen)");
        return new OwnTransferReceiptActions(driver);
    }
}
