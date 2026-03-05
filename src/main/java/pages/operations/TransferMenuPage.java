package pages.operations;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import pages.transfers.own.OwnTransferPage;

public class TransferMenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(TransferMenuPage.class);

    // testTag: menu_transfer_entre_mis_cuentas (Box wrapper en MenuTransferScreen.kt)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"menu_transfer_entre_mis_cuentas\")")
    private WebElement btnEntreMisCuentas;

    public TransferMenuPage(AndroidDriver driver) { super(driver); }

    public OwnTransferPage clickEntreMisCuentas() {
        log.info("Seleccionando opción: Entre mis cuentas");
        click(btnEntreMisCuentas, "Botón Entre mis cuentas");
        return new OwnTransferPage(driver);
    }
}
