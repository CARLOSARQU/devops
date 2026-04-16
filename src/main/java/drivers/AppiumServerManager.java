package drivers;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;

public class AppiumServerManager {
    private static final Logger log = LogManager.getLogger(AppiumServerManager.class);
    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();

    public static void start() {
        log.info("Iniciando servidor Appium internamente...");
        File appiumLogFile = new File("target/appium-server.log");

        String appiumPath = System.getProperty("appium.executable",
                System.getenv("APPIUM_EXECUTABLE") != null
                        ? System.getenv("APPIUM_EXECUTABLE")
                        : "appium");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .withLogFile(appiumLogFile);

        if (!appiumPath.equals("appium")) {
            builder.withAppiumJS(new File(appiumPath));
        }

        AppiumDriverLocalService localService = builder.build();
        localService.start();
        service.set(localService);
        log.info("Servidor Appium corriendo en: {}", localService.getUrl());
        log.info("Logs de Appium guardados en: {}", appiumLogFile.getAbsolutePath());
    }

    public static URL getServiceUrl() {
        AppiumDriverLocalService localService = service.get();
        if (localService == null) {
            throw new IllegalStateException("El servidor Appium no ha sido iniciado.");
        }
        return localService.getUrl();
    }

    public static void stop() {
        AppiumDriverLocalService localService = service.get();
        if (localService != null) {
            log.info("Apagando servidor Appium...");
            localService.stop();
            service.remove();
            log.info("Servidor Appium apagado correctamente.");
        } else {
            log.warn("stop() llamado pero no había servidor Appium activo.");
        }
    }
}
