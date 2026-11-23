package com.automation.base;

import com.automation.driver.DriverFactory;
import com.automation.listeners.ExtentReportListener;
import com.automation.listeners.TestListener;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({TestListener.class, ExtentReportListener.class})
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());
    protected IOSDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        log.info("Test session started: {}", driver.getSessionId());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
        log.info("Test session ended");
    }
}
