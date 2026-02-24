package tests;

import drivers.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        // Inicializa el driver y limpia la app antes de cada test
        DriverManager.resetApp();
    }

    @AfterMethod
    public void tearDown() {
        // Cierra la sesión después de cada test para liberar recursos
        DriverManager.quitDriver();
    }
}
