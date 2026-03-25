package screens.transfers.own;

import io.appium.java_client.android.AndroidDriver;

public class OwnTransferDetailsActions extends OwnTransferDetailsScreen {

    public OwnTransferDetailsActions(AndroidDriver driver) {
        super(driver);
        waitForVisibility(originAccountCard, 20);
    }

    public boolean isLoaded() {
        return originAccountCard.isDisplayed();
    }

    public OwnTransferSummaryActions enterAmountAndContinue(String amount) {
        log.info("Ingresando monto: {}", amount);
        sendKeys(amountField, amount, "Campo Monto");
        driver.hideKeyboard();
        scrollToElement("btn_transfer_own_continue");
        click(btnContinue, "Botón Continuar (Detalles)");
        return new OwnTransferSummaryActions(driver);
    }
}
