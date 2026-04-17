package screens.transfers.own;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import screens.ScenarioContext;

public class OwnTransferSteps {
    private static final Logger log = LogManager.getLogger(OwnTransferSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    private OwnTransferDetailsActions detailsActions;
    private OwnTransferSummaryActions summaryActions;
    private OwnTransferReceiptActions receiptActions;

    @And("selecciono Entre mis cuentas")
    public void seleccionoEntreMisCuentas() {
        log.info("Paso: selecciono Entre mis cuentas");
        detailsActions = context.transferMenu.clickEntreMisCuentas();
    }

    @Then("la pantalla de detalles de transferencia carga")
    public void laPantallaDeDetallesCarga() {
        log.info("Paso: verificando pantalla de detalles de transferencia");
        Assert.assertTrue(detailsActions.isLoaded(), "La pantalla de detalles de transferencia no cargó");
    }

    @When("ingreso el monto {string} y continúo")
    public void ingresoElMontoYContinuo(String monto) {
        log.info("Paso: ingreso monto {} y continúo", monto);
        summaryActions = detailsActions.enterAmountAndContinue(monto);
    }

    @When("ingreso el monto invalido {string}")
    public void ingresoElMontoInvalido(String monto) {
        log.info("Paso: ingreso monto inválido '{}'", monto);
        detailsActions.enterAmount(monto);
    }

    @Then("aparece el mensaje de validación {string}")
    public void apareceElMensajeDeValidacion(String mensaje) {
        log.info("Paso: verificando mensaje de validación '{}'", mensaje);
        Assert.assertTrue(detailsActions.isMensajeValidacionVisible(mensaje),
            "El mensaje de validación '" + mensaje + "' no apareció");
    }

    @Then("la pantalla de resumen de transferencia carga")
    public void laPantallaDeResumenCarga() {
        log.info("Paso: verificando pantalla de resumen");
        Assert.assertTrue(summaryActions.isLoaded(), "La pantalla de resumen no cargó");
    }

    @When("confirmo la transferencia")
    public void confirmoLaTransferencia() {
        log.info("Paso: confirmo la transferencia");
        receiptActions = summaryActions.clickContinue();
    }

    @Then("el comprobante de transferencia exitosa aparece")
    public void elComprobanteDeTransferenciaAparece() {
        log.info("Paso: verificando comprobante de transferencia");
        Assert.assertTrue(receiptActions.isTransferenciaExitosa(), "El comprobante de transferencia exitosa no apareció");
        log.info("RESULTADO: Transferencia entre cuentas propias completada correctamente.");
    }
}
