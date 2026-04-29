package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LosAndesOtpActions extends LosAndesOtpScreen {

    public LosAndesOtpActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Esperando que aparezca el dialog OTP (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(btnEnviar));
            return true;
        } catch (Exception e) {
            log.warn("El dialog OTP no apareció a tiempo");
            return false;
        }
    }

    /**
     * El OTP se auto-rellena vía SMS y la pantalla navega automáticamente al comprobante
     * (~30 segundos). No se requiere ninguna interacción manual.
     */
    public LosAndesTransferReceiptActions esperarAutoEnvio() {
        log.info("Esperando auto-envío del OTP por SMS (max 40 seg)...");
        return new LosAndesTransferReceiptActions(driver);
    }
}
