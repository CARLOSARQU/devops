package screens.credit;

import base.BaseScreen;
import io.appium.java_client.android.AndroidDriver;

// Los botones de selección de crédito tienen IDs dinámicos (credit_selection_btn_${loanNumber})
// No se declaran con @AndroidFindBy — se buscan en tiempo de ejecución por regex en CreditSelectionActions
public class CreditSelectionScreen extends BaseScreen {

    public CreditSelectionScreen(AndroidDriver driver) { super(driver); }
}
