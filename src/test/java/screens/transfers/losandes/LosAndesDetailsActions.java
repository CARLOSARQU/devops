package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;

public class LosAndesDetailsActions extends LosAndesDetailsScreen {

    public LosAndesDetailsActions(AndroidDriver driver) {
        super(driver);
        waitForClickability(amountField, 15);
    }

    public boolean isLoaded() {
        return amountField.isDisplayed();
    }

    public LosAndesTransferSummaryActions enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        scrollToElement("btn_transfer_third_details_continue");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new LosAndesTransferSummaryActions(driver);
    }
}
