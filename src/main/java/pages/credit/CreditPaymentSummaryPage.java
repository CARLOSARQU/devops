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

public class CreditPaymentSummaryPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CreditPaymentSummaryPage.class);

    // testTag: credit_summary_pay_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_summary_pay_button\")")
    private WebElement btnPagarCuota;

    public CreditPaymentSummaryPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla 'Detalles de pago' cargó");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(btnPagarCuota));
            return true;
        } catch (Exception e) {
            log.warn("La pantalla 'Detalles de pago' no cargó a tiempo");
            return false;
        }
    }

    public CreditPaymentReceiptPage clickPagarCuota() {
        log.info("Confirmando pago en pantalla 'Detalles de pago'");
        click(btnPagarCuota, "Botón Pagar Cuota");
        return new CreditPaymentReceiptPage(driver);
    }
}
