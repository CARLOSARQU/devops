package tests;
import drivers.DriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import pages.HomePage;
import org.testng.annotations.DataProvider;
import utils.JsonReader;
import java.util.Map;

public class LoginTest extends BaseTest {
    private WelcomePage welcomePage;
    private LoginPage loginPage;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        welcomePage = new WelcomePage(DriverManager.getDriver());
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return JsonReader.getTestData("src/test/resources/testdata/login_data.json");
    }

    @Test(dataProvider = "loginData", description = "Pruebas de Login Data-Driven")
    public void testLogin(Map<String, String> data) {
        System.out.println("Ejecutando Test: " + data.get("testCase"));

        // Flujo común: Ir a login
        loginPage = welcomePage.irALogin();

        if (data.get("esperado").equals("exitoso")) {
            HomePage home = loginPage.loginSuccessful(data.get("usuario"), data.get("clave"));
            Assert.assertTrue(home.isHomePageDisplayed(), "Home no visible para " + data.get("usuario"));
        } else {
            loginPage.login(data.get("usuario"), data.get("clave"));
            Assert.assertTrue(loginPage.isErrorModalDisplayed(), "Debe aparecer el modal de error para " + data.get("usuario"));
            loginPage.cerrarModalError();
        }
    }
}
