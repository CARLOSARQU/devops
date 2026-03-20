package steps;

import drivers.DriverManager;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import pages.transfers.own.OwnTransferDetailsPage;
import pages.transfers.own.OwnTransferReceiptPage;
import pages.transfers.own.OwnTransferSummaryPage;

public class OwnTransferSteps {
    private static final Logger log = LogManager.getLogger(OwnTransferSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    private OwnTransferDetailsPage detailsPage;
    private OwnTransferSummaryPage summaryPage;
    private OwnTransferReceiptPage receiptPage;

    @Y("selecciono Entre mis cuentas")
    public void seleccionoEntreMisCuentas() {
        log.info("Paso: selecciono Entre mis cuentas");
        detailsPage = context.transferMenu.clickEntreMisCuentas();
    }

    @Entonces("la pantalla de detalles de transferencia carga")
    public void laPantallaDeDetallesCarga() {
        log.info("Paso: verificando pantalla de detalles de transferencia");
        Assert.assertTrue(detailsPage.isLoaded(), "La pantalla de detalles de transferencia no cargó");
    }

    @Cuando("ingreso el monto {string} y continúo")
    public void ingresoElMontoYContinuo(String monto) {
        log.info("Paso: ingreso monto {} y continúo", monto);
        summaryPage = detailsPage.enterAmountAndContinue(monto);
    }

    @Entonces("la pantalla de resumen de transferencia carga")
    public void laPantallaDeResumenCarga() {
        log.info("Paso: verificando pantalla de resumen");
        Assert.assertTrue(summaryPage.isLoaded(), "La pantalla de resumen no cargó");
    }

    @Cuando("confirmo la transferencia")
    public void confirmoLaTransferencia() {
        log.info("Paso: confirmo la transferencia");
        receiptPage = summaryPage.clickContinue();
    }

    @Entonces("el comprobante de transferencia exitosa aparece")
    public void elComprobanteDeTransferenciaAparece() {
        log.info("Paso: verificando comprobante de transferencia");
        Assert.assertTrue(receiptPage.isTransferenciaExitosa(), "El comprobante de transferencia exitosa no apareció");
        log.info("RESULTADO: Transferencia entre cuentas propias completada correctamente.");
    }
}
