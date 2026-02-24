package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @AndroidFindBy(xpath = "//android.widget.EditText[1]")
    private WebElement dniField;

    @AndroidFindBy(xpath = "//android.widget.EditText[2]") 
    private WebElement passwordField;

    @AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.View[2]/android.widget.Button")
    private WebElement loginButtonFinal;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Datos incorrectos\"]")
    private WebElement tituloErrorModal;

    @AndroidFindBy(className = "android.widget.Button")
    private WebElement btnEntendidoErrorModal;

    public LoginPage(AndroidDriver driver) { super(driver); }

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

    public void login(String dni, String password) {
        enterDNI(dni);
        enterPassword(password);
        clickLogin();
    }

    public boolean isErrorModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(tituloErrorModal));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void cerrarModalError() {
        click(btnEntendidoErrorModal, "Cerrar Modal de Error");
    }
}
