package com.automation.listeners;

import com.automation.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int currentAttempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = ConfigReader.getInstance().getInt("retry.maxAttempts");
        if (currentAttempt < maxRetries) {
            currentAttempt++;
            return true;
        }
        return false;
    }
}
