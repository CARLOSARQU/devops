package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("TEST FALLIDO: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST EXITOSO: {}", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST OMITIDO: {}", result.getName());
    }
}