package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage extends BasePage {
    private static final Logger log = LogManager.getLogger(HomePage.class);

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Aviso importante']")
    private WebElement modalAvisoImportante;

    @AndroidFindBy(xpath = "//android.widget.Button")
    private WebElement btnEntendidoModal;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Mis productos']")
    private WebElement txtMisProductos;

    public HomePage(AndroidDriver driver) {
        super(driver);
        gestionarModalAviso();
    }

    private void gestionarModalAviso() {
        log.info("--- Verificando posible modal 'Aviso importante' ---");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOf(modalAvisoImportante));
            log.info("Modal 'Aviso importante' detectado. Procediendo a cerrar...");
            btnEntendidoModal.click();
            log.info("Modal cerrado satisfactoriamente.");
        } catch (Exception e) {
            log.info("No se presentó el modal 'Aviso importante', el flujo continúa.");
        }
    }

    public boolean isHomePageDisplayed() {
        log.info("Validando si la Home Page se muestra correctamente");
        return isDisplayed(txtMisProductos);
    }
}