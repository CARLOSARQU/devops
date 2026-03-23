package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import pages.credit.AccountSelectionPage;
import pages.credit.CreditMenuPage;
import pages.credit.CreditPaymentReceiptPage;
import pages.credit.CreditPaymentSummaryPage;
import pages.credit.CreditSelectionPage;
import pages.operations.PayCreditMenuPage;

public class CreditPaymentSteps {
    private static final Logger log = LogManager.getLogger(CreditPaymentSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    private PayCreditMenuPage payCreditMenu;
    private CreditSelectionPage creditSelectionPage;
    private CreditMenuPage creditMenuPage;
    private AccountSelectionPage accountSelectionPage;
    private CreditPaymentSummaryPage summaryPage;
    private CreditPaymentReceiptPage receiptPage;

    @And("selecciono Pagar Crédito")
    public void seleccionoPagarCredito() {
        log.info("Paso: selecciono Pagar Crédito");
        payCreditMenu = context.operationMenu.clickPagarCredito();
    }

    @Then("la pantalla de selección de crédito a pagar carga")
    public void laPantallaDeSeleccionDeCreditoCarga() {
        log.info("Paso: verificando pantalla de selección de crédito");
        Assert.assertTrue(payCreditMenu.isLoaded(), "La pantalla 'Elige el crédito a pagar' no cargó");
    }

    @When("selecciono Paga un crédito propio")
    public void seleccionoPagaUnCreditoPropio() {
        log.info("Paso: selecciono Paga un crédito propio");
        creditSelectionPage = payCreditMenu.clickPagarCreditoPropio();
    }

    @Then("los créditos cargan correctamente")
    public void losCreditosCorganCorrectamente() {
        log.info("Paso: verificando que los créditos cargaron");
        Assert.assertTrue(creditSelectionPage.isLoaded(), "Los créditos no cargaron en 15 seg");
    }

    @When("selecciono el primer crédito")
    public void seleccionoElPrimerCredito() {
        log.info("Paso: selecciono el primer crédito");
        creditMenuPage = creditSelectionPage.selectFirstCredit();
    }

    @Then("el menú del crédito carga")
    public void elMenuDelCreditoCarga() {
        log.info("Paso: verificando menú del crédito");
        Assert.assertTrue(creditMenuPage.isLoaded(), "El menú 'Paga tu crédito' no cargó");
    }

    @When("selecciono Pago de cuota")
    public void seleccionoPagoDeCuota() {
        log.info("Paso: selecciono Pago de cuota");
        accountSelectionPage = creditMenuPage.clickPagarCuota();
    }

    @Then("la pantalla de pago de cuota carga")
    public void laPantallaDePagoDeCuotaCarga() {
        log.info("Paso: verificando pantalla de pago de cuota");
        Assert.assertTrue(accountSelectionPage.isLoaded(), "La pantalla 'Pago de cuota' no cargó");
    }

    @When("confirmo el detalle de pago")
    public void confirmoElDetalleDePago() {
        log.info("Paso: confirmo el detalle de pago");
        summaryPage = accountSelectionPage.clickVerDetallesDePago();
    }

    @Then("la pantalla de detalles de pago carga")
    public void laPantallaDeDetallesDePagoCarga() {
        log.info("Paso: verificando pantalla de detalles de pago");
        Assert.assertTrue(summaryPage.isLoaded(), "La pantalla 'Detalles de pago' no cargó");
    }

    @When("realizo el pago de la cuota")
    public void realizoElPagoDeLaCuota() {
        log.info("Paso: realizo el pago de la cuota");
        receiptPage = summaryPage.clickPagarCuota();
    }

    @Then("el comprobante de pago exitoso aparece")
    public void elComprobanteDePagoExitosoAparece() {
        log.info("Paso: verificando comprobante de pago");
        Assert.assertTrue(receiptPage.isPagoExitoso(), "El comprobante de pago exitoso no apareció en 45 seg");
        log.info("RESULTADO: Pago total de cuota completado correctamente.");
    }
}
