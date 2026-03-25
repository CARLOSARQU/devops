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

    public LosAndesOtpActions clickContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        scrollToElement("btn_transfer_third_summary_continue");
        click(btnContinue, "Botón Continuar (Resumen)");
        return new LosAndesOtpActions(driver);
    }
}
