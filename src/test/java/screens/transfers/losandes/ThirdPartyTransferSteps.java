package screens.transfers.losandes;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import screens.ScenarioContext;
import utils.ConfigReader;

public class ThirdPartyTransferSteps {
    private static final Logger log = LogManager.getLogger(ThirdPartyTransferSteps.class);
    private final ScenarioContext context = ScenarioContext.getInstance();

    private LosAndesAccountEntryActions accountEntryActions;
    private LosAndesDetailsActions detailsActions;
    private LosAndesTransferSummaryActions summaryActions;
    private LosAndesOtpActions otpActions;
    private LosAndesTransferReceiptActions receiptActions;

    @And("selecciono A otras cuentas Los Andes")
    public void seleccionoAOtrasCuentasAndes() {
        log.info("Paso: selecciono A otras cuentas Los Andes");
        accountEntryActions = context.transferMenu.clickOtrasCuentasAndes();
    }

    @Then("la pantalla de ingreso de cuenta carga")
    public void laPantallaDeIngresoDeCuentaCarga() {
        log.info("Paso: verificando pantalla de ingreso de cuenta");
        Assert.assertTrue(accountEntryActions.isLoaded(), "La pantalla de ingreso de cuenta no cargó");
    }

    @When("ingreso el número de cuenta destino y continúo")
    public void ingresoElNumeroDeCuentaDestinoYContinuo() {
        String cuenta = ConfigReader.getProperty("test.third.party.account");
        log.info("Paso: ingreso cuenta destino y continúo");
        detailsActions = accountEntryActions.enterAccountNumberAndContinue(cuenta);
    }

    @Then("la pantalla de detalles carga")
    public void laPantallaDeDetallesCarga() {
        log.info("Paso: verificando pantalla de detalles");
        Assert.assertTrue(detailsActions.isLoaded(), "La pantalla de detalles no cargó");
    }

    @When("ingreso el monto {string} en transferencia a terceros y continúo")
    public void ingresoElMontoEnTransferenciaATercerosYContinuo(String monto) {
        log.info("Paso: ingreso monto {} en transferencia a terceros", monto);
        summaryActions = detailsActions.enterAmountAndContinue(monto);
    }

    @Then("la pantalla de resumen de transferencia a terceros carga")
    public void laPantallaDeResumenATercerosCarga() {
        log.info("Paso: verificando pantalla de resumen transferencia a terceros");
        Assert.assertTrue(summaryActions.isLoaded(), "La pantalla de resumen no cargó");
    }

    @When("confirmo la transferencia a terceros")
    public void confirmoLaTransferenciaATerceros() {
        log.info("Paso: confirmo transferencia a terceros");
        otpActions = summaryActions.clickContinue();
    }

    @Then("la pantalla OTP aparece")
    public void laPantallaOTPAparece() {
        log.info("Paso: verificando pantalla OTP");
        Assert.assertTrue(otpActions.isLoaded(), "El dialog OTP no apareció");
    }

    @When("envío el OTP")
    public void envioElOTP() {
        log.info("Paso: envío OTP");
        receiptActions = otpActions.clickEnviar();
    }

    @Then("el comprobante de transferencia a terceros aparece")
    public void elComprobanteDeTransferenciaATercerosAparece() {
        log.info("Paso: verificando comprobante de transferencia a terceros");
        Assert.assertTrue(receiptActions.isTransferenciaExitosa(), "El comprobante de transferencia exitosa no apareció");
        log.info("RESULTADO: Transferencia a otras cuentas Los Andes completada correctamente.");
    }
}
