package tests;
import drivers.DriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import pages.HomePage;

public class LoginTest {
    private WelcomePage welcomePage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        welcomePage = new WelcomePage(DriverManager.getDriver());
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Test(description = "Login fallido con credenciales inválidas", priority = 1)
    public void testLoginFallido() {
        // Va al login saltando los permisos
        LoginPage loginPage = welcomePage.irALogin();

        // Ingresa datos falsos
        loginPage.login("00000000", "000000");

        // Valida que aparezca el modal
        Assert.assertTrue(loginPage.isErrorModalDisplayed(), "Debe aparecer el modal de 'Datos incorrectos'");

        // Cierra el modal
        loginPage.cerrarModalError();
    }

    @Test(description = "Login exitoso")
    public void testLoginExitoso() {
        // La magia ocurre aquí: irALogin() maneja todos los permisos por ti y te devuelve la pantalla de login
        HomePage home = welcomePage.irALogin()
                .loginSuccessful("71313648", "140304");

        Assert.assertTrue(home.isHomePageDisplayed(), "Home no visible");
    }
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
