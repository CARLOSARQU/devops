package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class WelcomePage extends BasePage {
    /// 1. El modal que vimos vacío
    @AndroidFindBy(className = "android.widget.Button")
    private WebElement btnEntendido;

    // 2. Permiso Ubicación (Usa el ID nativo de Android)
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_foreground_only_button")
    private WebElement btnPermisoUbicacion;

    // 3. Permisos Generales (Notificaciones y SMS usan el mismo ID nativo)
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    private WebElement btnPermisoGeneral;

    // 4. El botón de Iniciar Sesión de la bienvenida (Usamos la estructura que viste en el inspector)
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.Button\").instance(0)")
    private WebElement btnIniciarSesionWelcome;

    public WelcomePage(AndroidDriver driver) { super(driver); }

    // Este método agrupa toda la barrera de permisos iniciales
    public WelcomePage gestionarOnboardingYPermisos() {
        System.out.println("--- Iniciando gestión de Onboarding y Permisos ---");
        clickIfPresent(btnEntendido, "Botón Entendido (Modal)");
        clickIfPresent(btnPermisoUbicacion, "Permiso Ubicación");
        clickIfPresent(btnPermisoGeneral, "Permiso Notificaciones");
        clickIfPresent(btnPermisoGeneral, "Permiso SMS");
        return this;
    }

    // Este método es el puente que te lleva a la página de Login Real
    public LoginPage irALogin() {
        gestionarOnboardingYPermisos();
        click(btnIniciarSesionWelcome, "Botón Iniciar Sesión (Bienvenida)");
        return new LoginPage(driver);
    }
}
