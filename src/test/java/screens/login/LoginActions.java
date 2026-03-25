package screens.login;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import screens.home.HomeActions;
import java.time.Duration;

public class LoginActions extends LoginScreen {

    public LoginActions(AndroidDriver driver) { super(driver); }

    public HomeActions loginSuccessful(String dni, String password) {
        log.info("Ejecutando flujo de Login Exitoso");
        sendKeys(dniField, dni, "Campo DNI");
        sendPassword(passwordField, password, "Campo Contraseña");
        driver.hideKeyboard();
        click(btnLogin, "Botón Ingresar");
        return new HomeActions(driver);
    }

    public void login(String dni, String password) {
        log.info("Ejecutando flujo de Login Genérico");
        sendKeys(dniField, dni, "Campo DNI");
        sendPassword(passwordField, password, "Campo Contraseña");
        driver.hideKeyboard();
        click(btnLogin, "Botón Ingresar");
    }

    public boolean ingresarClaveDigitalSiAparece(String password) {
        log.info("Verificando si aparece la pantalla de clave digital...");
        try {
            waitForVisibility(passwordField, 5);
            log.info("Pantalla de clave digital detectada — ingresando clave");
            sendPassword(passwordField, password, "Campo Clave Digital");
            driver.hideKeyboard();
            click(btnLogin, "Botón Confirmar Clave Digital");
            return true;
        } catch (Exception e) {
            log.info("Pantalla de clave digital no apareció — continuando");
            return false;
        }
    }

    public boolean isErrorModalDisplayed() {
        log.info("Validando si el modal de error es visible");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(tituloErrorModal));
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
