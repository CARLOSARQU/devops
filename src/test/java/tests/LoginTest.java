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

    @Test(description = "Login exitoso")
    public void testLoginExitoso() {
        loginPage.completarOnboarding();
        HomePage home = welcomePage.clickIniciarSesion().loginSuccessful("71313648", "140304");
        Assert.assertTrue(home.isHomePageDisplayed(), "Home no visible");
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
