package screens.transfers.own;

import io.appium.java_client.android.AndroidDriver;

public class OwnTransferSummaryActions extends OwnTransferSummaryScreen {

    public OwnTransferSummaryActions(AndroidDriver driver) {
        super(driver);
        waitForVisibility(btnContinue, 20);
    }

    public boolean isLoaded() {
        return btnContinue.isDisplayed();
    }

    public OwnTransferReceiptActions clickContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        click(btnContinue, "Botón Continuar (Resumen)");
        return new OwnTransferReceiptActions(driver);
    }
}
