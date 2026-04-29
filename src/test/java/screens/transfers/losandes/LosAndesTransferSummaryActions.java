package screens.transfers.losandes;

import io.appium.java_client.android.AndroidDriver;

public class LosAndesTransferSummaryActions extends LosAndesTransferSummaryScreen {

    public LosAndesTransferSummaryActions(AndroidDriver driver) {
        super(driver);
        waitForVisibility(btnContinue, 20);
    }

    public boolean isLoaded() {
        return btnContinue.isDisplayed();
    }

    public LosAndesTransferReceiptActions clickContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        scrollToElement("btn_transfer_thirdcla_summary_continuar");
        click(btnContinue, "Botón Continuar (Resumen)");
        return new LosAndesTransferReceiptActions(driver);
    }
}
