package pages.transfers.own;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;
import pages.transfers.own.OwnTransferReceiptPage;
import java.time.Duration;

public class OwnTransferSummaryPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OwnTransferSummaryPage.class);

    // testTag: transfer_summary_continue_button
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"transfer_summary_continue_button\")")
    private WebElement btnContinue;

    public OwnTransferSummaryPage(AndroidDriver driver) { super(driver); }

    public boolean isLoaded() {
        log.info("Verificando que la pantalla de resumen cargó (max 20 seg — espera respuesta API)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOf(btnContinue));
            return true;
        } catch (Exception e) {
            log.warn("Pantalla de resumen no cargó a tiempo");
            return false;
        }
    }

    public OwnTransferReceiptPage clickContinue() {
        log.info("Confirmando transferencia en pantalla de resumen");
        click(btnContinue, "Botón Continuar (Resumen)");
        return new OwnTransferReceiptPage(driver);
    }
}
