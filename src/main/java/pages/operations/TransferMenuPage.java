package pages.operations;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import pages.transfers.own.OwnTransferPage;
import pages.transfers.thirdparty.ThirdPartyAccountEntryPage;

public class TransferMenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(TransferMenuPage.class);

    // testTag: menu_transfer_entre_mis_cuentas
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"menu_transfer_entre_mis_cuentas\")")
    private WebElement btnEntreMisCuentas;

    // testTag: menu_transfer_otras_cuentas_andes
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"menu_transfer_otras_cuentas_andes\")")
    private WebElement btnOtrasCuentasAndes;

    public TransferMenuPage(AndroidDriver driver) { super(driver); }

    public OwnTransferPage clickEntreMisCuentas() {
        log.info("Seleccionando opción: Entre mis cuentas");
        click(btnEntreMisCuentas, "Botón Entre mis cuentas");
        return new OwnTransferPage(driver);
    }

    public ThirdPartyAccountEntryPage clickOtrasCuentasAndes() {
        log.info("Seleccionando opción: A otras cuentas Los Andes");
        click(btnOtrasCuentasAndes, "Botón Otras cuentas Los Andes");
        return new ThirdPartyAccountEntryPage(driver);
    }
}
