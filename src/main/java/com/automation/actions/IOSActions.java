package com.automation.actions;

import com.automation.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public final class IOSActions {

    private static final Logger log = LogManager.getLogger(IOSActions.class);

    private IOSActions() {}

    public static void hideKeyboard() {
        try {
            DriverManager.getDriver().hideKeyboard();
            log.debug("Keyboard hidden");
        } catch (Exception e) {
            log.debug("Keyboard was not visible");
        }
    }

    public static void runAppInBackground(Duration duration) {
        DriverManager.getDriver().runAppInBackground(duration);
        log.debug("App sent to background for {} seconds", duration.getSeconds());
    }

    public static void activateApp(String bundleId) {
        DriverManager.getDriver().activateApp(bundleId);
        log.debug("Activated app: {}", bundleId);
    }

    public static void terminateApp(String bundleId) {
        DriverManager.getDriver().terminateApp(bundleId);
        log.debug("Terminated app: {}", bundleId);
    }

    public static boolean isAppInstalled(String bundleId) {
        return DriverManager.getDriver().isAppInstalled(bundleId);
    }

    public static void setClipboard(String text) {
        DriverManager.getDriver().setClipboardText(text);
    }

    public static String getClipboard() {
        return DriverManager.getDriver().getClipboardText();
    }

    public static void shake() {
        IOSDriver driver = DriverManager.getDriver();
        driver.shake();
        log.debug("Device shaken");
    }

    public static void lockDevice(Duration duration) {
        DriverManager.getDriver().lockDevice(duration);
        log.debug("Device locked for {} seconds", duration.getSeconds());
    }

    public static void unlockDevice() {
        DriverManager.getDriver().unlockDevice();
        log.debug("Device unlocked");
    }
}
