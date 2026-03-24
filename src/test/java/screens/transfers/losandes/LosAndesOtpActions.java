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

    public LosAndesTransferReceiptActions clickEnviar() {
        log.info("Haciendo clic en Enviar (OTP auto-completado por SMS)");
        click(btnEnviar, "Botón Enviar OTP");
        return new LosAndesTransferReceiptActions(driver);
    }
}
