package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;

public class LosAndesAccountEntryActions extends LosAndesAccountEntryScreen {

    public LosAndesAccountEntryActions(AndroidDriver driver) {
        super(driver);
        waitForVisibility(accountNumberField, 10);
    }

    public boolean isLoaded() {
        return accountNumberField.isDisplayed();
    }

    public LosAndesDetailsActions enterAccountNumberAndContinue(String accountNumber) {
        log.info("Ingresando número de cuenta destino: {}", accountNumber);
        sendKeys(accountNumberField, accountNumber, "Campo Número de Cuenta");
        driver.hideKeyboard();
        click(btnContinue, "Botón Continuar (Ingreso de Cuenta)");
        return new LosAndesDetailsActions(driver);
    }
}
