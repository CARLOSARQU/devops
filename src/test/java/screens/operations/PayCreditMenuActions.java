package screens.operations;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import screens.credit.CreditSelectionActions;
import java.time.Duration;

public class PayCreditMenuActions extends PayCreditMenuScreen {

    public PayCreditMenuActions(AndroidDriver driver) { super(driver); }

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

    public CreditSelectionActions clickPagarCreditoPropio() {
        log.info("Seleccionando opción: Paga un crédito propio");
        click(btnPagarCreditoPropio, "Botón Paga un crédito propio");
        return new CreditSelectionActions(driver);
    }
}
