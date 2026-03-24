package screens;

import screens.home.HomeActions;
import screens.operations.OperationMenuActions;
import screens.operations.TransferMenuActions;

public class ScenarioContext {
    private static ScenarioContext instance = new ScenarioContext();
    private static boolean isLoggedIn = false;

    public HomeActions homePage;
    public OperationMenuActions operationMenu;
    public TransferMenuActions transferMenu;

    public static ScenarioContext getInstance() { return instance; }
    public static boolean isLoggedIn() { return isLoggedIn; }
    public static void setLoggedIn(boolean value) { isLoggedIn = value; }

    // reset() solo limpia el contexto del escenario actual, no la sesión
    public static void reset() { instance = new ScenarioContext(); }
}
