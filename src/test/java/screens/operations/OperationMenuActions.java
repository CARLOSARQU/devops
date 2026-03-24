package screens.operations;

import io.appium.java_client.android.AndroidDriver;

public class OperationMenuActions extends OperationMenuScreen {

    public OperationMenuActions(AndroidDriver driver) { super(driver); }

    public TransferMenuActions clickTransferencias() {
        log.info("Seleccionando opción: Transferencias");
        click(btnTransferencias, "Botón Transferencias");
        return new TransferMenuActions(driver);
    }

    public PayCreditMenuActions clickPagarCredito() {
        log.info("Seleccionando opción: Pagar Crédito");
        click(btnPagarCredito, "Botón Pagar Crédito");
        return new PayCreditMenuActions(driver);
    }
}
