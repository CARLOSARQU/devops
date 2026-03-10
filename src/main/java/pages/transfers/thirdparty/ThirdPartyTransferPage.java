package pages.transfers.thirdparty;

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

public class ThirdPartyTransferPage extends BasePage {

    private static final Logger log = LogManager.getLogger(ThirdPartyTransferPage.class);

    // ── Paso 1: AccountEntryScreen ───────────────────────────────────────────

    // testTag: third_party_account_number_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_number_field\")")
    private WebElement accountNumberField;

    // testTag: third_party_account_entry_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_entry_continue_button\")")
    private WebElement btnAccountEntryContinue;

    // ── Paso 2: DetailsScreen ────────────────────────────────────────────────

    // testTag: third_party_amount_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_amount_field\")")
    private WebElement amountField;

    // testTag: third_party_details_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_details_continue_button\")")
    private WebElement btnDetailsContinue;

    // ── Paso 3: SummaryScreen ────────────────────────────────────────────────

    // testTag: third_party_summary_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_summary_continue_button\")")
    private WebElement btnSummaryContinue;

    // ── OTP Dialog (DialogOtpScreen del design system AAR) ───────────────────
    // Renderiza en ventana separada (Dialog de Compose) — sin testTags propios
    // Se localiza por texto del botón "Enviar"
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Enviar\")")
    private WebElement btnOtpEnviar;

    // ────────────────────────────────────────────────────────────────────────

    public ThirdPartyTransferPage(AndroidDriver driver) { super(driver); }

    // ── Paso 1 ───────────────────────────────────────────────────────────────

    public boolean isAccountEntryScreenLoaded() {
        log.info("Verificando que la pantalla de ingreso de cuenta cargó");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOf(accountNumberField));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de ingreso de cuenta no cargó a tiempo");
            return false;
        }
    }

    public ThirdPartyTransferPage enterAccountNumber(String accountNumber) {
        log.info("Ingresando número de cuenta destino: {}", accountNumber);
        sendKeys(accountNumberField, accountNumber, "Campo Número de Cuenta");
        driver.hideKeyboard();
        return this;
    }

    public ThirdPartyTransferPage clickAccountEntryContinue() {
        log.info("Haciendo clic en Continuar (ingreso de cuenta)");
        click(btnAccountEntryContinue, "Botón Continuar (Ingreso de Cuenta)");
        return this;
    }

    // ── Paso 2 ───────────────────────────────────────────────────────────────

    public boolean isDetailsScreenLoaded() {
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

    public ThirdPartyTransferPage enterAmount(String amount) {
        log.info("Ingresando monto de transferencia: {}", amount);
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        return this;
    }

    public ThirdPartyTransferPage clickDetailsContinue() {
        log.info("Haciendo clic en Continuar (detalles)");
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        scrollToElement("third_party_details_continue_button");
        click(btnDetailsContinue, "Botón Continuar (Detalles)");
        return this;
    }

    // ── Paso 3 ───────────────────────────────────────────────────────────────

    public boolean isSummaryScreenLoaded() {
        log.info("Verificando que la pantalla de resumen cargó (max 20 seg — espera respuesta API)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(btnSummaryContinue));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de resumen no cargó a tiempo");
            return false;
        }
    }

    public ThirdPartyTransferPage clickSummaryContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        scrollToElement("third_party_summary_continue_button");
        click(btnSummaryContinue, "Botón Continuar (Resumen)");
        return this;
    }

    // ── OTP ──────────────────────────────────────────────────────────────────

    public boolean isOtpDialogLoaded() {
        log.info("Esperando que aparezca el dialog OTP (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(btnOtpEnviar));
            return true;
        } catch (Exception e) {
            log.warn("El dialog OTP no apareció a tiempo");
            return false;
        }
    }

    public ThirdPartyTransferReceiptPage clickOtpEnviar() {
        log.info("Haciendo clic en Enviar (OTP auto-completado por SMS)");
        click(btnOtpEnviar, "Botón Enviar OTP");
        return new ThirdPartyTransferReceiptPage(driver);
    }

    // ── Receipt ──────────────────────────────────────────────────────────────

    public ThirdPartyTransferReceiptPage confirmAndGetReceipt() {
        clickSummaryContinue();
        return new ThirdPartyTransferReceiptPage(driver);
    }
}
