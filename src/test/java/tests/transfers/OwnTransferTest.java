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
import pages.transfers.own.OwnTransferPage;
import pages.transfers.TransferReceiptPage;
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
        OwnTransferPage transferPage = transferMenu.clickEntreMisCuentas();

        // 4. Verificar que la pantalla de detalles cargó (max 10 seg)
        Assert.assertTrue(transferPage.isDetailsScreenLoaded(),
                "La pantalla de detalles de transferencia no cargó");

        // 5. Ingresar monto — "100" equivale a S/ 1.00
        transferPage.enterAmount("100");

        // 6. Hacer clic en Continuar (hace scroll automáticamente al botón)
        transferPage.clickContinuar();

        // 7. Verificar que la pantalla de resumen cargó (max 20 seg — espera respuesta API)
        Assert.assertTrue(transferPage.isSummaryScreenLoaded(),
                "La pantalla de resumen no cargó después del primer Continuar");

        // 8. Confirmar la transferencia
        TransferReceiptPage receipt = transferPage.clickContinuarSummary();

        // 9. Verificar comprobante
        Assert.assertTrue(receipt.isTransferenciaExitosa(),
                "El comprobante de transferencia exitosa no apareció");

        log.info("RESULTADO: Transferencia entre cuentas propias completada correctamente.");
    }
}
