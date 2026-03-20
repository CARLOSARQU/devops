package steps;

import pages.home.HomePage;
import pages.operations.OperationMenuPage;
import pages.operations.TransferMenuPage;

public class ScenarioContext {
    private static ScenarioContext instance = new ScenarioContext();

    public HomePage homePage;
    public OperationMenuPage operationMenu;
    public TransferMenuPage transferMenu;

    public static ScenarioContext getInstance() { return instance; }
    public static void reset() { instance = new ScenarioContext(); }
}
