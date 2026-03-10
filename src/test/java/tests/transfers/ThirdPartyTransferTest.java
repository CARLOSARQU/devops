package tests.transfers;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;
import pages.operations.OperationMenuPage;
import pages.operations.TransferMenuPage;
import pages.transfers.thirdparty.ThirdPartyAccountEntryPage;
import pages.transfers.thirdparty.ThirdPartyDetailsPage;
import pages.transfers.thirdparty.ThirdPartyOtpPage;
import pages.transfers.thirdparty.ThirdPartyTransferReceiptPage;
import pages.transfers.thirdparty.ThirdPartyTransferSummaryPage;
import tests.BaseTest;
import utils.ConfigReader;

public class ThirdPartyTransferTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(ThirdPartyTransferTest.class);
    private HomePage homePage;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        String dni      = ConfigReader.getProperty("test.dni");
        String password = ConfigReader.getProperty("test.password");

        WelcomePage welcomePage = new WelcomePage(DriverManager.getDriver());
        LoginPage loginPage     = welcomePage.irALogin();
        homePage                = loginPage.loginSuccessful(dni, password);
    }

    @Test(description = "Transferencia a otras cuentas Los Andes por S/ 1.00")
    public void testTransferenciaOtrasCuentasAndes() {
        log.info("========== TEST: Transferencia A Otras Cuentas Los Andes ==========");

        String destinationAccount = ConfigReader.getProperty("test.third.party.account");

        // 1. Ir a Operaciones
        homePage.irAOperaciones();
        OperationMenuPage operationMenu = new OperationMenuPage(DriverManager.getDriver());

        // 2. Seleccionar Transferencias
        TransferMenuPage transferMenu = operationMenu.clickTransferencias();

        // 3. Seleccionar A otras cuentas Los Andes
        ThirdPartyAccountEntryPage accountEntryPage = transferMenu.clickOtrasCuentasAndes();

        // 4. Verificar pantalla de ingreso de cuenta e ingresar número
        Assert.assertTrue(accountEntryPage.isLoaded(),
                "La pantalla de ingreso de cuenta no cargó");
        ThirdPartyDetailsPage detailsPage = accountEntryPage.enterAccountNumberAndContinue(destinationAccount);

        // 5. Verificar pantalla de detalles (espera API) e ingresar monto
        Assert.assertTrue(detailsPage.isLoaded(),
                "La pantalla de detalles no cargó después de ingresar la cuenta");
        ThirdPartyTransferSummaryPage summaryPage = detailsPage.enterAmountAndContinue("100");

        // 6. Verificar pantalla de resumen (espera API) y confirmar
        Assert.assertTrue(summaryPage.isLoaded(),
                "La pantalla de resumen no cargó");
        ThirdPartyOtpPage otpPage = summaryPage.clickContinue();

        // 7. Esperar dialog OTP (código llega por SMS y se autocompleta) y enviar
        Assert.assertTrue(otpPage.isLoaded(),
                "El dialog OTP no apareció");
        ThirdPartyTransferReceiptPage receiptPage = otpPage.clickEnviar();

        // 8. Verificar comprobante de transferencia exitosa
        Assert.assertTrue(receiptPage.isTransferenciaExitosa(),
                "El comprobante de transferencia exitosa no apareció");

        log.info("RESULTADO: Transferencia a otras cuentas Los Andes completada correctamente.");
    }
}
