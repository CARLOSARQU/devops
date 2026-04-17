package screens.login;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LoginScreen extends BaseScreen {

    // testTag: field_login_dni a txt_login_login_dni
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"txt_login_login_dni\")")
    protected WebElement dniField;

    // testTag: field_login_password a txt_login_login_clave
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"txt_login_login_clave\")")
    protected WebElement passwordField;

    // testTag: btn_login_submit a btn_login_login_ingresar
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_login_login_ingresar\")")
    protected WebElement btnLogin;

    // Dialog de credenciales incorrectas
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Datos incorrectos\"]")
    protected WebElement tituloErrorModal;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Entendido\")")
    protected WebElement btnEntendidoErrorModal;

    public LoginScreen(AndroidDriver driver) { super(driver); }
}
