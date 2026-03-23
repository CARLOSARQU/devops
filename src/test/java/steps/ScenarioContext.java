package steps;

import pages.home.HomePage;
import pages.operations.OperationMenuPage;
import pages.operations.TransferMenuPage;

public class ScenarioContext {
    private static ScenarioContext instance = new ScenarioContext();
    private static boolean isLoggedIn = false;

    public HomePage homePage;
    public OperationMenuPage operationMenu;
    public TransferMenuPage transferMenu;

    public static ScenarioContext getInstance() { return instance; }
    public static boolean isLoggedIn() { return isLoggedIn; }
    public static void setLoggedIn(boolean value) { isLoggedIn = value; }

    // reset() solo limpia el contexto del escenario actual, no la sesión
    public static void reset() { instance = new ScenarioContext(); }
}
