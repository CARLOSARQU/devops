package screens.onboarding;

import io.appium.java_client.android.AndroidDriver;
import screens.login.LoginActions;

public class WelcomeActions extends WelcomeScreen {

    public WelcomeActions(AndroidDriver driver) { super(driver); }

    public LoginActions irALogin() {
        log.info("--- Iniciando gestión de Onboarding y Permisos ---");
        clickIfPresent(btnEntendido, "Botón Entendido (Modal)");
        clickIfPresent(btnPermisoUbicacion, "Permiso Ubicación");
        clickIfPresent(btnPermisoGeneral, "Permiso Notificaciones");
        clickIfPresent(btnPermisoGeneral, "Permiso SMS");
        log.info("Navegando hacia la pantalla de Login");
        click(btnIniciarSesion, "Botón Iniciar Sesión (Bienvenida)");
        return new LoginActions(driver);
    }
}
