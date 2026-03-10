package pages.transfers.thirdparty;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class ThirdPartyAccountEntryPage extends BasePage {

    private static final Logger log = LogManager.getLogger(ThirdPartyAccountEntryPage.class);

    // testTag: third_party_account_number_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_number_field\")")
    private WebElement accountNumberField;

    // testTag: third_party_account_entry_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"third_party_account_entry_continue_button\")")
    private WebElement btnContinue;

    public ThirdPartyAccountEntryPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
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

    public ThirdPartyDetailsPage enterAccountNumberAndContinue(String accountNumber) {
        log.info("Ingresando número de cuenta destino: {}", accountNumber);
        sendKeys(accountNumberField, accountNumber, "Campo Número de Cuenta");
        driver.hideKeyboard();
        click(btnContinue, "Botón Continuar (Ingreso de Cuenta)");
        return new ThirdPartyDetailsPage(driver);
    }
}
