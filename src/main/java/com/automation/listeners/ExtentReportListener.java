package com.automation.listeners;

import com.automation.reporting.ExtentManager;
import com.automation.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        test.log(Status.FAIL, result.getThrowable());

        String screenshotPath = ScreenshotUtil.capture(result.getMethod().getMethodName() + "_FAIL");
        if (screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP,
                result.getThrowable() != null ? result.getThrowable().getMessage() : "Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
        ExtentManager.removeTest();
    }
}
