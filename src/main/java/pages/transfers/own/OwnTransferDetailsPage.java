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

public class OwnTransferDetailsPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OwnTransferDetailsPage.class);

    // testTag: transfer_origin_account_card
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_origin_account_card\")")
    private WebElement originAccountCard;

    // testTag: transfer_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_amount_field\")")
    private WebElement amountField;

    // testTag: transfer_continue_button (al fondo del scroll)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_continue_button\")")
    private WebElement btnContinue;

    public OwnTransferDetailsPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de detalles de transferencia cargó");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(originAccountCard));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de detalles no cargó a tiempo");
            return false;
        }
    }

    public OwnTransferSummaryPage enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        scrollToElement("transfer_continue_button");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new OwnTransferSummaryPage(driver);
    }
}
