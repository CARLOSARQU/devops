package screens.credit;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AccountSelectionActions extends AccountSelectionScreen {

    public AccountSelectionActions(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla 'Pago de cuota' cargó (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(btnConfirmarPago));
            return true;
        } catch (Exception e) {
            log.warn("La pantalla 'Pago de cuota' no cargó a tiempo");
            return false;
        }
    }

    public CreditPaymentSummaryActions clickVerDetallesDePago() {
        log.info("Esperando que 'Ver detalles de pago' se habilite (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(btnConfirmarPago));
        } catch (Exception e) {
            log.warn("El botón 'Ver detalles de pago' no se habilitó en el tiempo esperado");
        }
        scrollToElement("btn_credit_account_confirm_pay");
        click(btnConfirmarPago, "Botón Ver detalles de pago");
        return new CreditPaymentSummaryActions(driver);
    }
}
