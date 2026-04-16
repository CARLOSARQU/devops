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

    // testTag: btn_onboarding_welcome_iniciar a btn_onboarding_welcome_iniciar
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_onboarding_welcome_iniciar\")")
    protected WebElement btnIniciarSesion;

    // testTag: btn_onboarding_welcome_registrar, para cuando se agregue test de registro
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"btn_onboarding_welcome_registar\")")
    protected WebElement btnRegistrar;

    public WelcomeScreen(AndroidDriver driver) { super(driver); }
}
