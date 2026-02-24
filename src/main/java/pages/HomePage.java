package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {
    // Evitamos el acento directamente para que Jenkins no falle al compilar
    @AndroidFindBy(xpath = "//*[contains(@text, 'Cerrar') or contains(@text, 'CERRAR')]") 
    private WebElement btnCerrarSesion;

    public HomePage(AndroidDriver driver) { super(driver); }

    public boolean isHomePageDisplayed() { return isDisplayed(btnCerrarSesion); }
}
