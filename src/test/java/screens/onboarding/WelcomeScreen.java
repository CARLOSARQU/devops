package screens.onboarding;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class WelcomeScreen extends BaseScreen {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Entendido\")")
    protected WebElement btnEntendido;

    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_foreground_only_button")
    protected WebElement btnPermisoUbicacion;

    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    protected WebElement btnPermisoGeneral;

    // testTag: btn_welcome_login
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_welcome_login\")")
    protected WebElement btnIniciarSesion;

    public WelcomeScreen(AndroidDriver driver) { super(driver); }
}
