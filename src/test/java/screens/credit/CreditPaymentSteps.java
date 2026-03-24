package screens.credit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import screens.ScenarioContext;
import screens.operations.PayCreditMenuActions;

public class CreditPaymentSteps {
    private static final Logger log = LogManager.getLogger(CreditPaymentSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    private PayCreditMenuActions payCreditMenu;
    private CreditSelectionActions creditSelectionActions;
    private CreditMenuActions creditMenuActions;
    private AccountSelectionActions accountSelectionActions;
    private CreditPaymentSummaryActions summaryActions;
    private CreditPaymentReceiptActions receiptActions;

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
        creditSelectionActions = payCreditMenu.clickPagarCreditoPropio();
    }

    @Then("los créditos cargan correctamente")
    public void losCreditosCorganCorrectamente() {
        log.info("Paso: verificando que los créditos cargaron");
        Assert.assertTrue(creditSelectionActions.isLoaded(), "Los créditos no cargaron en 15 seg");
    }

    @When("selecciono el primer crédito")
    public void seleccionoElPrimerCredito() {
        log.info("Paso: selecciono el primer crédito");
        creditMenuActions = creditSelectionActions.selectFirstCredit();
    }

    @Then("el menú del crédito carga")
    public void elMenuDelCreditoCarga() {
        log.info("Paso: verificando menú del crédito");
        Assert.assertTrue(creditMenuActions.isLoaded(), "El menú 'Paga tu crédito' no cargó");
    }

    @When("selecciono Pago de cuota")
    public void seleccionoPagoDeCuota() {
        log.info("Paso: selecciono Pago de cuota");
        accountSelectionActions = creditMenuActions.clickPagarCuota();
    }

    @Then("la pantalla de pago de cuota carga")
    public void laPantallaDePagoDeCuotaCarga() {
        log.info("Paso: verificando pantalla de pago de cuota");
        Assert.assertTrue(accountSelectionActions.isLoaded(), "La pantalla 'Pago de cuota' no cargó");
    }

    @When("confirmo el detalle de pago")
    public void confirmoElDetalleDePago() {
        log.info("Paso: confirmo el detalle de pago");
        summaryActions = accountSelectionActions.clickVerDetallesDePago();
    }

    @Then("la pantalla de detalles de pago carga")
    public void laPantallaDeDetallesDePagoCarga() {
        log.info("Paso: verificando pantalla de detalles de pago");
        Assert.assertTrue(summaryActions.isLoaded(), "La pantalla 'Detalles de pago' no cargó");
    }

    @When("realizo el pago de la cuota")
    public void realizoElPagoDeLaCuota() {
        log.info("Paso: realizo el pago de la cuota");
        receiptActions = summaryActions.clickPagarCuota();
    }

    @Then("el comprobante de pago exitoso aparece")
    public void elComprobanteDePagoExitosoAparece() {
        log.info("Paso: verificando comprobante de pago");
        Assert.assertTrue(receiptActions.isPagoExitoso(), "El comprobante de pago exitoso no apareció en 45 seg");
        log.info("RESULTADO: Pago total de cuota completado correctamente.");
    }
}
