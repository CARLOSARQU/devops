package utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar config.properties");
        }
    }
    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isEmpty()) return systemProperty;
        return properties.getProperty(key);
    }
}
