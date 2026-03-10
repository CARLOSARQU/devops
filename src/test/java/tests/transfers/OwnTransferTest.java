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
import pages.transfers.own.OwnTransferDetailsPage;
import pages.transfers.own.OwnTransferReceiptPage;
import pages.transfers.own.OwnTransferSummaryPage;
import tests.BaseTest;
import utils.ConfigReader;

public class OwnTransferTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(OwnTransferTest.class);
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

    @Test(description = "Transferencia entre cuentas propias por S/ 1.00")
    public void testTransferenciaEntreMisCuentas() {
        log.info("========== TEST: Transferencia Entre Mis Cuentas ==========");

        // 1. Ir a Operaciones desde el bottom nav
        homePage.irAOperaciones();
        OperationMenuPage operationMenu = new OperationMenuPage(DriverManager.getDriver());

        // 2. Seleccionar Transferencias
        TransferMenuPage transferMenu = operationMenu.clickTransferencias();

        // 3. Seleccionar Entre mis cuentas
        OwnTransferDetailsPage detailsPage = transferMenu.clickEntreMisCuentas();

        // 4. Verificar pantalla de detalles e ingresar monto — "100" equivale a S/ 1.00
        Assert.assertTrue(detailsPage.isLoaded(),
                "La pantalla de detalles de transferencia no cargó");
        OwnTransferSummaryPage summaryPage = detailsPage.enterAmountAndContinue("100");

        // 5. Verificar pantalla de resumen (max 20 seg — espera respuesta API) y confirmar
        Assert.assertTrue(summaryPage.isLoaded(),
                "La pantalla de resumen no cargó");
        OwnTransferReceiptPage receiptPage = summaryPage.clickContinue();

        // 6. Verificar comprobante
        Assert.assertTrue(receiptPage.isTransferenciaExitosa(),
                "El comprobante de transferencia exitosa no apareció");

        log.info("RESULTADO: Transferencia entre cuentas propias completada correctamente.");
    }
}
