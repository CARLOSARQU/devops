package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LosAndesTransferReceiptActions extends LosAndesTransferReceiptScreen {

    public LosAndesTransferReceiptActions(AndroidDriver driver) { super(driver); }

    public boolean isTransferenciaExitosa() {
        log.info("Verificando comprobante de transferencia exitosa");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(txtTransferenciaExitosa));
            log.info("Comprobante de transferencia a otras cuentas Los Andes confirmado.");
            return true;
        } catch (Exception e) {
            log.warn("El comprobante de transferencia no apareció");
            return false;
        }
    }
}
