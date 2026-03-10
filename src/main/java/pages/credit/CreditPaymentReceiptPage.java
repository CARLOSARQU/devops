package pages.credit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class CreditPaymentReceiptPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CreditPaymentReceiptPage.class);

    // testTag: credit_receipt_success_title — texto "¡Pago exitoso!"
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_receipt_success_title\")")
    private WebElement txtPagoExitoso;

    // testTag: credit_receipt_go_home_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_receipt_go_home_button\")")
    private WebElement btnIrAlInicio;

    public CreditPaymentReceiptPage(AndroidDriver driver) { super(driver); }

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
