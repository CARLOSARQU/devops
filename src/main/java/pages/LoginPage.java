package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);

    @AndroidFindBy(xpath = "//android.widget.EditText[1]")
    private WebElement dniField;

    @AndroidFindBy(xpath = "//android.widget.EditText[2]") 
    private WebElement passwordField;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.Button\").instance(1)")
    private WebElement loginButtonFinal;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Datos incorrectos\"]")
    private WebElement tituloErrorModal;

    @AndroidFindBy(className = "android.widget.Button")
    private WebElement btnEntendidoErrorModal;

    public LoginPage(AndroidDriver driver) { super(driver); }

    public LoginPage enterDNI(String dni) {
        log.info("Ingresando DNI en el campo correspondiente");
        sendKeys(dniField, dni, "Campo DNI");
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Ingresando clave secreta");
        sendPassword(passwordField, password, "Campo Contraseña");
        return this;
    }

    public void clickLogin() {
        log.info("Solicitando inicio de sesión");
        driver.hideKeyboard();
        click(loginButtonFinal, "Boton Ingresar");
    }
    public HomePage loginSuccessful(String dni, String password) {
        log.info("Ejecutando flujo de Login Exitoso");
        enterDNI(dni).enterPassword(password).clickLogin();
        return new HomePage(driver);
    }

    public void login(String dni, String password) {
        log.info("Ejecutando flujo de Login Genérico");
        enterDNI(dni);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginButtonEnabled() {
        return loginButtonFinal.isEnabled();
    }

    public boolean isErrorModalDisplayed() {
        log.info("Validando si el modal de error es visible");
        try {
            wait.until(ExpectedConditions.visibilityOf(tituloErrorModal));
            return true;
        } catch (Exception e) {
            log.warn("El modal de error no apareció");
            return false;
        }
    }
    public void cerrarModalError() {
        log.info("Confirmando lectura de error y cerrando modal");
        click(btnEntendidoErrorModal, "Cerrar Modal de Error");
    }
}
