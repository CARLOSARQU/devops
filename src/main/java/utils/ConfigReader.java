package utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {
    protected static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String DEFAULT_ENV = "qa";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();
        String fileName = "src/test/resources/" + env + ".properties";
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
            log.info("--- Configuración cargada desde: " + fileName + " ---");
        } catch (IOException e) {
            if (!env.equals(DEFAULT_ENV)) {
                log.error("Archivo " + fileName + " no encontrado. Usando " + DEFAULT_ENV + " por defecto.");
                String fallback = "src/test/resources/" + DEFAULT_ENV + ".properties";
                try (FileInputStream fis = new FileInputStream(fallback)) {
                    properties.load(fis);
                    log.info("--- Configuración cargada desde: " + fallback + " ---");
                } catch (IOException ex) {
                    throw new RuntimeException("No se pudo cargar ningún archivo de propiedades.", ex);
                }
            } else {
                throw new RuntimeException("No se pudo cargar ningún archivo de propiedades.", e);
            }
        }
    }

    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isEmpty()) return systemProperty;
        return properties.getProperty(key);
    }
}
