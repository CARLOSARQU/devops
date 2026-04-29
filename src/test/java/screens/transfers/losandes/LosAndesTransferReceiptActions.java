package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LosAndesTransferReceiptActions extends LosAndesTransferReceiptScreen {

    public LosAndesTransferReceiptActions(AndroidDriver driver) { super(driver); }

    /**
     * Espera hasta 40s porque el OTP se auto-rellena desde SMS (~30s) y luego navega aquí.
     */
    public boolean isTransferenciaExitosa() {
        log.info("Esperando comprobante de transferencia exitosa (max 40 seg — incluye auto-OTP)...");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(ExpectedConditions.visibilityOf(txtTransferenciaExitosa));
            log.info("Comprobante de transferencia a otras cuentas Los Andes confirmado.");
            return true;
        } catch (Exception e) {
            log.warn("El comprobante de transferencia no apareció en 40 seg");
            return false;
        }
    }

    public void clickCompartir() {
        click(btnCompartir, "Botón Compartir comprobante");
    }

    public void clickRealizarOtraOperacion() {
        click(btnRealizarOtraOperacion, "Botón Realizar otra operación");
    }
}
