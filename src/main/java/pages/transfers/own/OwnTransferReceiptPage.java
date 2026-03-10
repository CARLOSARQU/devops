package pages.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class OwnTransferReceiptPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OwnTransferReceiptPage.class);

    // testTag: transfer_receipt_title — texto "¡Transferencia exitosa!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_receipt_title\")")
    private WebElement txtTransferenciaExitosa;

    public OwnTransferReceiptPage(AndroidDriver driver) { super(driver); }

    public boolean isTransferenciaExitosa() {
        log.info("Verificando comprobante de transferencia exitosa");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(txtTransferenciaExitosa));
            log.info("Comprobante de transferencia exitosa confirmado.");
            return true;
        } catch (Exception e) {
            log.warn("El comprobante de transferencia no apareció");
            return false;
        }
    }
}
