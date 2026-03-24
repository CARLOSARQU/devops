package screens.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OwnTransferReceiptActions extends OwnTransferReceiptScreen {

    public OwnTransferReceiptActions(AndroidDriver driver) { super(driver); }

    public boolean isTransferenciaExitosa() {
        log.info("Verificando comprobante de transferencia exitosa");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(25))
                    .until(ExpectedConditions.visibilityOf(txtTransferenciaExitosa));
            log.info("Comprobante de transferencia exitosa confirmado.");
            return true;
        } catch (Exception e) {
            log.warn("El comprobante de transferencia no apareció");
            return false;
        }
    }
}
