package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LosAndesAccountEntryActions extends LosAndesAccountEntryScreen {

    public LosAndesAccountEntryActions(AndroidDriver driver) { super(driver); }

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

    public LosAndesDetailsActions enterAccountNumberAndContinue(String accountNumber) {
        log.info("Ingresando número de cuenta destino: {}", accountNumber);
        sendKeys(accountNumberField, accountNumber, "Campo Número de Cuenta");
        driver.hideKeyboard();
        click(btnContinue, "Botón Continuar (Ingreso de Cuenta)");
        return new LosAndesDetailsActions(driver);
    }
}
