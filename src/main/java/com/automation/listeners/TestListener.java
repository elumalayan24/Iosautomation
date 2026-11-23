package com.automation.listeners;

import com.automation.config.ConfigReader;
import com.automation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("Test suite started: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {} ({}ms)",
                result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());

        if (ConfigReader.getInstance().getBoolean("screenshots.onPass")) {
            ScreenshotUtil.capture(result.getMethod().getMethodName() + "_PASS");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {} - {}", result.getMethod().getMethodName(), result.getThrowable().getMessage());

        if (ConfigReader.getInstance().getBoolean("screenshots.onFailure")) {
            ScreenshotUtil.capture(result.getMethod().getMethodName() + "_FAIL");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {} - {}", result.getMethod().getMethodName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "no reason");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test suite finished: {} | Passed: {} | Failed: {} | Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }
}
