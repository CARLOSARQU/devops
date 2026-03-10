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

public class AccountSelectionPage extends BasePage {

    private static final Logger log = LogManager.getLogger(AccountSelectionPage.class);

    // testTag: account_selection_confirm_pay_button — siempre renderizado, se habilita cuando la API carga
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"account_selection_confirm_pay_button\")")
    private WebElement btnConfirmarPago;

    public AccountSelectionPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        // El botón "Ver detalles de pago" siempre está renderizado (aunque deshabilitado).
        // Es la señal más temprana de que la pantalla 'Pago de cuota' cargó.
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

    public CreditPaymentSummaryPage clickVerDetallesDePago() {
        // Espera a que el botón se habilite (cuando la API devuelve datos de cuota y cuenta).
        log.info("Esperando que 'Ver detalles de pago' se habilite (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(btnConfirmarPago));
        } catch (Exception e) {
            log.warn("El botón 'Ver detalles de pago' no se habilitó en el tiempo esperado");
        }
        scrollToElement("account_selection_confirm_pay_button");
        click(btnConfirmarPago, "Botón Ver detalles de pago");
        return new CreditPaymentSummaryPage(driver);
    }
}
