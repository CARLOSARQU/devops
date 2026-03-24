package screens.credit;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CreditPaymentReceiptActions extends CreditPaymentReceiptScreen {

    public CreditPaymentReceiptActions(AndroidDriver driver) { super(driver); }

    public boolean isPagoExitoso() {
        log.info("Verificando comprobante de pago exitoso (max 45 seg — procesamiento backend)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(45))
                    .until(ExpectedConditions.visibilityOf(txtPagoExitoso));
            log.info("Comprobante de pago exitoso confirmado.");
            return true;
        } catch (Exception e) {
            log.warn("El comprobante de pago exitoso no apareció en 45 seg");
            return false;
        }
    }
}
