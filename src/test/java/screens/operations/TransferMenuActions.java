package screens.operations;

import io.appium.java_client.android.AndroidDriver;
import screens.transfers.own.OwnTransferDetailsActions;
import screens.transfers.losandes.LosAndesAccountEntryActions;

public class TransferMenuActions extends TransferMenuScreen {

    public TransferMenuActions(AndroidDriver driver) {
        super(driver);
        waitForVisibility(btnEntreMisCuentas, 10);
    }

    public OwnTransferDetailsActions clickEntreMisCuentas() {
        log.info("Seleccionando opción: Entre mis cuentas");
        click(btnEntreMisCuentas, "Botón Entre mis cuentas");
        return new OwnTransferDetailsActions(driver);
    }

    public LosAndesAccountEntryActions clickOtrasCuentasAndes() {
        log.info("Seleccionando opción: A otras cuentas Los Andes");
        click(btnOtrasCuentasAndes, "Botón Otras cuentas Los Andes");
        return new LosAndesAccountEntryActions(driver);
    }
}
