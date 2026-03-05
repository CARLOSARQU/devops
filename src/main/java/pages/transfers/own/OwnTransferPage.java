package pages.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import pages.transfers.TransferReceiptPage;
import java.time.Duration;

public class OwnTransferPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OwnTransferPage.class);

    // ── TransferDetailsScreen ────────────────────────────────────────────────

    // testTag: transfer_origin_account_card
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_origin_account_card\")")
    private WebElement originAccountCard;

    // testTag: transfer_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_amount_field\")")
    private WebElement amountField;

    // testTag: transfer_destination_account_card
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_destination_account_card\")")
    private WebElement destinationAccountCard;

    // testTag: transfer_continue_button (al fondo del scroll en TransferDetailsScreen)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_continue_button\")")
    private WebElement btnContinuar;

    // ── TransferSummaryScreen ────────────────────────────────────────────────

    // testTag: transfer_summary_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_summary_continue_button\")")
    private WebElement btnContinuarSummary;

    // ────────────────────────────────────────────────────────────────────────

    public OwnTransferPage(AndroidDriver driver) { super(driver); }

    public boolean isDetailsScreenLoaded() {
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

    public OwnTransferPage enterAmount(String amount) {
        log.info("Ingresando monto de transferencia: {}", amount);
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        return this;
    }

    // El botón Continuar está al fondo del scroll — se hace scroll antes de hacer clic
    public OwnTransferPage clickContinuar() {
        log.info("Haciendo clic en Continuar (pantalla de detalles)");
        scrollToElement("transfer_continue_button");
        click(btnContinuar, "Botón Continuar");
        return this;
    }

    public boolean isSummaryScreenLoaded() {
        log.info("Verificando que la pantalla de resumen cargó (max 20 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(btnContinuarSummary));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de resumen no cargó a tiempo");
            return false;
        }
    }

    public TransferReceiptPage clickContinuarSummary() {
        log.info("Confirmando transferencia en pantalla de resumen");
        click(btnContinuarSummary, "Botón Continuar (Resumen)");
        return new TransferReceiptPage(driver);
    }
}
