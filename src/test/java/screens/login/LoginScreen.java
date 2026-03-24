package screens.login;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LoginScreen extends BaseScreen {

    // testTag: login_dni_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_dni_field\")")
    protected WebElement dniField;

    // testTag: login_password_field
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_password_field\")")
    protected WebElement passwordField;

    // testTag: login_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"login_button\")")
    protected WebElement btnLogin;

    // Dialog de credenciales incorrectas
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Datos incorrectos\"]")
    protected WebElement tituloErrorModal;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Entendido\")")
    protected WebElement btnEntendidoErrorModal;

    public LoginScreen(AndroidDriver driver) { super(driver); }
}
