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
import pages.transfers.thirdparty.ThirdPartyTransferPage;
import pages.transfers.thirdparty.ThirdPartyTransferReceiptPage;
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
        ThirdPartyTransferPage transferPage = transferMenu.clickOtrasCuentasAndes();

        // 4. Verificar que cargó la pantalla de ingreso de cuenta (Paso 1)
        Assert.assertTrue(transferPage.isAccountEntryScreenLoaded(),
                "La pantalla de ingreso de cuenta no cargó");

        // 5. Ingresar número de cuenta destino y continuar
        transferPage.enterAccountNumber(destinationAccount)
                    .clickAccountEntryContinue();

        // 6. Verificar que cargó la pantalla de detalles (Paso 2 — espera API)
        Assert.assertTrue(transferPage.isDetailsScreenLoaded(),
                "La pantalla de detalles no cargó después de ingresar la cuenta");

        // 7. Ingresar monto — "100" equivale a S/ 1.00
        transferPage.enterAmount("100")
                    .clickDetailsContinue();

        // 8. Verificar que cargó la pantalla de resumen (Paso 3 — espera API)
        Assert.assertTrue(transferPage.isSummaryScreenLoaded(),
                "La pantalla de resumen no cargó");

        // 9. Confirmar — dispara envío del OTP por SMS
        transferPage.clickSummaryContinue();

        // 10. Esperar dialog OTP (código llega por SMS y se autocompleta)
        Assert.assertTrue(transferPage.isOtpDialogLoaded(),
                "El dialog OTP no apareció");

        // 11. Enviar OTP y verificar comprobante
        ThirdPartyTransferReceiptPage receipt = transferPage.clickOtpEnviar();

        Assert.assertTrue(receipt.isTransferenciaExitosa(),
                "El comprobante de transferencia exitosa no apareció");

        log.info("RESULTADO: Transferencia a otras cuentas Los Andes completada correctamente.");
    }
}
