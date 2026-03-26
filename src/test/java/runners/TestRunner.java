package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {
                "classpath:features/01_login.feature",
                "classpath:features/02_home.feature",
                "classpath:features/03_own_transfer.feature",
                "classpath:features/04_third_party_transfer.feature",
                "classpath:features/05_credit_payment.feature"
        },
        glue = {"screens", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/report.html",
                "json:target/cucumber-reports/report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
