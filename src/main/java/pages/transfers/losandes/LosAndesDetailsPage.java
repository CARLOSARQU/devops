package pages.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class LosAndesDetailsPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LosAndesDetailsPage.class);

    // testTag: third_party_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_amount_field\")")
    private WebElement amountField;

    // testTag: third_party_details_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_details_continue_button\")")
    private WebElement btnContinue;

    public LosAndesDetailsPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de detalles cargó (max 15 seg — espera respuesta API)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(amountField));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de detalles no cargó a tiempo");
            return false;
        }
    }

    public LosAndesTransferSummaryPage enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        scrollToElement("third_party_details_continue_button");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new LosAndesTransferSummaryPage(driver);
    }
}
