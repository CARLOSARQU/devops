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

public class CreditMenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CreditMenuPage.class);

    // testTag: credit_menu_pago_cuota
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"credit_menu_pago_cuota\")")
    private WebElement btnPagarCuota;

    public CreditMenuPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que el menú 'Paga tu crédito' cargó (max 10 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(btnPagarCuota));
            return true;
        } catch (Exception e) {
            log.warn("El menú 'Paga tu crédito' no cargó a tiempo");
            return false;
        }
    }

    public AccountSelectionPage clickPagarCuota() {
        log.info("Seleccionando opción 'Pago de cuota'");
        click(btnPagarCuota, "Botón Pago de Cuota");
        return new AccountSelectionPage(driver);
    }
}
