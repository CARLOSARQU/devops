package pages.onboarding;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.BasePage;
import pages.login.LoginPage;

public class WelcomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(WelcomePage.class);

    // Modal genérico opcional (safety net para diálogos del sistema)
    @AndroidFindBy(className = "android.widget.Button")
    private WebElement btnEntendido;

    // Permiso Ubicación (ID nativo de Android)
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_foreground_only_button")
    private WebElement btnPermisoUbicacion;

    // Permisos Generales: Notificaciones y SMS (ID nativo de Android)
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    private WebElement btnPermisoGeneral;

    // testTag: welcome_login_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"welcome_login_button\")")
    private WebElement btnIniciarSesionWelcome;

    public WelcomePage(AndroidDriver driver) { super(driver); }

    public WelcomePage gestionarOnboardingYPermisos() {
        log.info("--- Iniciando gestión de Onboarding y Permisos ---");
        clickIfPresent(btnEntendido, "Botón Entendido (Modal)");
        clickIfPresent(btnPermisoUbicacion, "Permiso Ubicación");
        clickIfPresent(btnPermisoGeneral, "Permiso Notificaciones");
        clickIfPresent(btnPermisoGeneral, "Permiso SMS");
        return this;
    }

    public LoginPage irALogin() {
        gestionarOnboardingYPermisos();
        log.info("Navegando hacia la pantalla de Login");
        click(btnIniciarSesionWelcome, "Botón Iniciar Sesión (Bienvenida)");
        return new LoginPage(driver);
    }
}
