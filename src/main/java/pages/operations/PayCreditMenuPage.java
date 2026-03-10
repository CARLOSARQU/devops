package pages.operations;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import pages.credit.CreditSelectionPage;
import java.time.Duration;

public class PayCreditMenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(PayCreditMenuPage.class);

    // testTag: pay_credit_own_button — botón "Paga un crédito propio" en PayCreditScreen
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"pay_credit_own_button\")")
    private WebElement btnPagarCreditoPropio;

    public PayCreditMenuPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de tipo de pago de crédito cargó");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(btnPagarCreditoPropio));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de tipo de pago de crédito no cargó a tiempo");
            return false;
        }
    }

    public CreditSelectionPage clickPagarCreditoPropio() {
        log.info("Seleccionando opción: Paga un crédito propio");
        click(btnPagarCreditoPropio, "Botón Paga un crédito propio");
        return new CreditSelectionPage(driver);
    }
}
