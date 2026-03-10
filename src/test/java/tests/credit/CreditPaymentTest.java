package tests.credit;

import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.credit.AccountSelectionPage;
import pages.credit.CreditMenuPage;
import pages.credit.CreditPaymentReceiptPage;
import pages.credit.CreditPaymentSummaryPage;
import pages.credit.CreditSelectionPage;
import pages.home.HomePage;
import pages.login.LoginPage;
import pages.onboarding.WelcomePage;
import pages.operations.OperationMenuPage;
import pages.operations.PayCreditMenuPage;
import tests.BaseTest;
import utils.ConfigReader;

public class CreditPaymentTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(CreditPaymentTest.class);
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

    @Test(description = "Pago total de cuota de crédito vía Operaciones")
    public void testPagoTotalDeCuota() {
        log.info("========== TEST: Pago Total de Cuota de Crédito ==========");

        // 1. Ir a Operaciones desde el bottom nav
        homePage.irAOperaciones();
        OperationMenuPage operationMenu = new OperationMenuPage(DriverManager.getDriver());

        // 2. Seleccionar "Pagar Crédito"
        PayCreditMenuPage payCreditMenu = operationMenu.clickPagarCredito();
        Assert.assertTrue(payCreditMenu.isLoaded(),
                "La pantalla 'Elige el crédito a pagar' no cargó");

        // 3. Seleccionar "Paga un crédito propio"
        CreditSelectionPage creditSelectionPage = payCreditMenu.clickPagarCreditoPropio();

        // 4. Esperar max 15 seg a que carguen los créditos y seleccionar el primero
        Assert.assertTrue(creditSelectionPage.isLoaded(),
                "Los créditos no cargaron en 15 seg (pantalla 'Mis créditos')");
        CreditMenuPage creditMenuPage = creditSelectionPage.selectFirstCredit();

        // 5. Esperar max 10 seg a que cargue el menú y seleccionar "Pago de cuota"
        Assert.assertTrue(creditMenuPage.isLoaded(),
                "El menú 'Paga tu crédito' no cargó en 10 seg");
        AccountSelectionPage accountSelectionPage = creditMenuPage.clickPagarCuota();

        // 6. Esperar max 15 seg a que cargue la pantalla y confirmar
        Assert.assertTrue(accountSelectionPage.isLoaded(),
                "La pantalla 'Pago de cuota' no cargó en 15 seg");
        CreditPaymentSummaryPage summaryPage = accountSelectionPage.clickVerDetallesDePago();

        // 7. Verificar pantalla "Detalles de pago" y confirmar pago
        Assert.assertTrue(summaryPage.isLoaded(),
                "La pantalla 'Detalles de pago' no cargó");
        CreditPaymentReceiptPage receiptPage = summaryPage.clickPagarCuota();

        // 8. Esperar max 45 seg el comprobante de pago
        Assert.assertTrue(receiptPage.isPagoExitoso(),
                "El comprobante de pago exitoso no apareció en 45 seg");

        log.info("RESULTADO: Pago total de cuota completado correctamente.");
    }
}
