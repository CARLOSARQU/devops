package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    @AndroidFindBy(className = "android.widget.Button")

    private WebElement btnEntendido;

    @AndroidFindBy(xpath = "//android.widget.EditText[1]") 
    private WebElement dniField;

    @AndroidFindBy(xpath = "//android.widget.EditText[2]") 
    private WebElement passwordField;

    @AndroidFindBy(xpath = "//*[contains(@text, 'Iniciar') or contains(@text, 'INGRESAR')]")
    private WebElement loginButtonFinal;

    @AndroidFindBy(xpath = "//*[contains(@text, 'incorrectos')]")
    private WebElement tituloErrorModal;

    public LoginPage(AndroidDriver driver) { super(driver); }

    public LoginPage completarOnboarding() {
        try {
            click(btnEntendido, "Boton Entendido");
        } catch (Exception e) {
            System.out.println("No se encontró el modal de Onboarding, continuando...");
        }
        return this;
    }

    public LoginPage enterDNI(String dni) {
        sendKeys(dniField, dni, "Campo DNI");
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendPassword(passwordField, password, "Campo Contraseña");
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
