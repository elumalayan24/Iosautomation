package com.automation.driver;

import io.appium.java_client.ios.IOSDriver;

public final class DriverManager {

    private static final ThreadLocal<IOSDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static IOSDriver getDriver() {
        IOSDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized. Call DriverFactory.createDriver() first.");
        }
        return driver;
    }

    static void setDriver(IOSDriver driver) {
        DRIVER.set(driver);
    }

    public static void removeDriver() {
        DRIVER.remove();
    }

    public static boolean isDriverActive() {
        return DRIVER.get() != null;
    }
}
