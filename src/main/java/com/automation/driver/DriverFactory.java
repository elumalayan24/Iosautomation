package com.automation.driver;

import com.automation.config.ConfigReader;
import com.automation.config.ConfigValidator;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static IOSDriver createDriver() {
        ConfigValidator.validate();
        ConfigReader config = ConfigReader.getInstance();

        XCUITestOptions options = DriverOptionsFactory.createIOSOptions();
        IOSDriver driver = initializeDriver(config.getString("appium.serverUrl"), options);
        configureTimeouts(driver, config);

        DriverManager.setDriver(driver);
        log.info("Driver created for device: {}", config.getString("ios.deviceName"));
        return driver;
    }

    public static IOSDriver createDriver(String app) {
        ConfigValidator.validate();
        ConfigReader config = ConfigReader.getInstance();

        XCUITestOptions options = DriverOptionsFactory.createIOSOptions(app);
        IOSDriver driver = initializeDriver(config.getString("appium.serverUrl"), options);
        configureTimeouts(driver, config);

        DriverManager.setDriver(driver);
        log.info("Driver created with app: {}", app);
        return driver;
    }

    public static IOSDriver createDriverForBundle(String bundleId) {
        ConfigValidator.validate();
        ConfigReader config = ConfigReader.getInstance();

        XCUITestOptions options = DriverOptionsFactory.createIOSOptionsForBundleId(bundleId);
        IOSDriver driver = initializeDriver(config.getString("appium.serverUrl"), options);
        configureTimeouts(driver, config);

        DriverManager.setDriver(driver);
        log.info("Driver created for bundle: {}", bundleId);
        return driver;
    }

    public static void quitDriver() {
        if (DriverManager.isDriverActive()) {
            try {
                DriverManager.getDriver().quit();
                log.info("Driver session terminated");
            } catch (Exception e) {
                log.warn("Error while quitting driver: {}", e.getMessage());
            } finally {
                DriverManager.removeDriver();
            }
        }
    }

    private static IOSDriver initializeDriver(String serverUrl, XCUITestOptions options) {
        try {
            log.info("Connecting to Appium server at {}", serverUrl);
            return new IOSDriver(URI.create(serverUrl).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Appium server URL: " + serverUrl, e);
        }
    }

    private static void configureTimeouts(IOSDriver driver, ConfigReader config) {
        int implicitWait = config.getInt("timeouts.implicitWait");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
    }
}
