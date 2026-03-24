package screens.credit;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreditPaymentSummaryActions extends CreditPaymentSummaryScreen {

    public CreditPaymentSummaryActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla 'Detalles de pago' cargó (max 10 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(btnPagarCuota));
            return true;
        } catch (Exception e) {
            log.warn("La pantalla 'Detalles de pago' no cargó a tiempo");
            return false;
        }
    }

    public CreditPaymentReceiptActions clickPagarCuota() {
        log.info("Confirmando pago en pantalla 'Detalles de pago'");
        click(btnPagarCuota, "Botón Pagar Cuota");
        return new CreditPaymentReceiptActions(driver);
    }
}
