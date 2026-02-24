package pages;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {
    @AndroidFindBy(xpath = "//*[@text='Cerrar sesión']") private WebElement btnCerrarSesion;
    public HomePage(AndroidDriver driver) { super(driver); }
    public boolean isHomePageDisplayed() { return isDisplayed(btnCerrarSesion); }
}
