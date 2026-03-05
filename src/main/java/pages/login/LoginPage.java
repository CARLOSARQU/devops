package pages.login;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import pages.home.HomePage;
import java.time.Duration;

public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    // testTag: login_dni_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_dni_field\")")
    private WebElement dniField;

    // testTag: login_password_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_password_field\")")
    private WebElement passwordField;

    // testTag: login_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_button\")")
    private WebElement loginButtonFinal;

    // Dialog de credenciales incorrectas (sin testTag en design system AAR)
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Datos incorrectos\"]")
    private WebElement tituloErrorModal;

    // Botón "Entendido" del dialog de error (texto del string str_understood)
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Entendido\")")
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

    public boolean isLoginButtonClickable() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(loginButtonFinal));
            String clickable = loginButtonFinal.getAttribute("clickable");
            log.info("Atributo clickable del botón login: {}", clickable);
            return Boolean.parseBoolean(clickable);
        } catch (Exception e) {
            log.warn("Botón de login no encontrado");
            return false;
        }
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
