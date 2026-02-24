package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage extends BasePage {
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Entendido']") private WebElement btnEntendido;
    @AndroidFindBy(xpath = "//android.widget.EditText[1]") private WebElement dniField;
    @AndroidFindBy(xpath = "//android.widget.EditText[2]") private WebElement passwordField;
    @AndroidFindBy(xpath = "//*[@text='Iniciar sesion' or @text='INICIAR SESION']") private WebElement loginButtonFinal;
    @AndroidFindBy(xpath = "//*[@text='Datos incorrectos']") private WebElement tituloErrorModal;

    public LoginPage(AndroidDriver driver) { super(driver); }

    public LoginPage completarOnboarding() {
        try { click(btnEntendido, "Boton Entendido"); } catch (Exception e) {}
        return this;
    }

    public LoginPage enterDNI(String dni) {
        sendKeys(dniField, dni, "Campo DNI");
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendPassword(passwordField, password, "Campo Contrasena");
        return this;
    }

    public void clickLogin() { click(loginButtonFinal, "Boton Ingresar"); }

    public HomePage loginSuccessful(String dni, String password) {
        enterDNI(dni).enterPassword(password).clickLogin();
        return new HomePage(driver);
    }

    public boolean isErrorModalDisplayed() {
        try { wait.until(ExpectedConditions.visibilityOf(tituloErrorModal)); return true; } catch (Exception e) { return false; }
    }
    
    public void cerrarModalError() { try { click(btnEntendido, "Cerrar Error"); } catch (Exception e) {} }
}
