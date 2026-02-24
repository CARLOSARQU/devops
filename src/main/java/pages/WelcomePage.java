package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class WelcomePage extends BasePage {
    @AndroidFindBy(xpath = "//*[@text='Iniciar sesion']") private WebElement btnIniciarSesion;
    public WelcomePage(AndroidDriver driver) { super(driver); }
    public LoginPage clickIniciarSesion() { click(btnIniciarSesion, "Iniciar Sesion"); return new LoginPage(driver); }
    public boolean isWelcomePageDisplayed() { return isDisplayed(btnIniciarSesion); }
}
