package pages.transfers.thirdparty;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import java.time.Duration;

public class ThirdPartyOtpPage extends BasePage {

    private static final Logger log = LogManager.getLogger(ThirdPartyOtpPage.class);

    // Dialog OTP del design system AAR — renderiza en ventana separada (Compose Dialog).
    // Sin testTags propios. Se localiza por texto del botón.
    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Enviar\")")
    private WebElement btnEnviar;

    public ThirdPartyOtpPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Esperando que aparezca el dialog OTP (max 15 seg)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOf(btnEnviar));
            return true;
        } catch (Exception e) {
            log.warn("El dialog OTP no apareció a tiempo");
            return false;
        }
    }

    public ThirdPartyTransferReceiptPage clickEnviar() {
        log.info("Haciendo clic en Enviar (OTP auto-completado por SMS)");
        click(btnEnviar, "Botón Enviar OTP");
        return new ThirdPartyTransferReceiptPage(driver);
    }
}
