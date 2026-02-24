package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage extends BasePage {

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
        System.out.println("--- Esperando posible modal 'Aviso importante' ---");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(25))
                    .until(ExpectedConditions.visibilityOf(modalAvisoImportante));
            System.out.println("Modal 'Aviso importante' detectado. Cerrando...");
            btnEntendidoModal.click();
            System.out.println("Modal cerrado correctamente.");
        } catch (Exception e) {
            System.out.println("Modal 'Aviso importante' no apareció, continuando...");
        }
    }

    public boolean isHomePageDisplayed() {
        return isDisplayed(txtMisProductos);
    }
}