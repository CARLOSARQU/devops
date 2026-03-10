package pages.operations;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.BasePage;

public class OperationMenuPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OperationMenuPage.class);

    // testTag: operations_btn_transferencias (Box wrapper en OperationMenuScreen.kt)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"operations_btn_transferencias\")")
    private WebElement btnTransferencias;

    // testTag: operations_btn_pagar_credito (Box wrapper en OperationMenuScreen.kt)
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"operations_btn_pagar_credito\")")
    private WebElement btnPagarCredito;

    public OperationMenuPage(AndroidDriver driver) { super(driver); }

    public TransferMenuPage clickTransferencias() {
        log.info("Seleccionando opción: Transferencias");
        click(btnTransferencias, "Botón Transferencias");
        return new TransferMenuPage(driver);
    }

    public PayCreditMenuPage clickPagarCredito() {
        log.info("Seleccionando opción: Pagar Crédito");
        click(btnPagarCredito, "Botón Pagar Crédito");
        return new PayCreditMenuPage(driver);
    }
}
