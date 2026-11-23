package com.automation.utils;

import com.automation.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;

import java.util.Map;

public final class PlatformUtil {

    private static final Logger log = LogManager.getLogger(PlatformUtil.class);

    private PlatformUtil() {}

    public static String getDeviceName() {
        return getCapability("deviceName");
    }

    public static String getPlatformVersion() {
        return getCapability("platformVersion");
    }

    public static String getUdid() {
        return getCapability("udid");
    }

    public static Dimension getScreenSize() {
        return DriverManager.getDriver().manage().window().getSize();
    }

    public static int getScreenWidth() {
        return getScreenSize().getWidth();
    }

    public static int getScreenHeight() {
        return getScreenSize().getHeight();
    }

    public static boolean isSimulator() {
        String udid = getUdid();
        return udid != null && udid.contains("-");
    }

    public static Map<String, Object> getDeviceInfo() {
        IOSDriver driver = DriverManager.getDriver();
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) driver.executeScript("mobile: deviceInfo");
        return info;
    }

    public static String getDeviceTime() {
        return DriverManager.getDriver().getDeviceTime();
    }

    public static void setBatteryMonitoring(boolean enabled) {
        IOSDriver driver = DriverManager.getDriver();
        Map<String, Object> args = Map.of("state", enabled ? 1 : 0);
        driver.executeScript("mobile: setPasteboard", args);
        log.debug("Battery monitoring set to: {}", enabled);
    }

    private static String getCapability(String name) {
        Object value = DriverManager.getDriver().getCapabilities().getCapability(name);
        return value != null ? value.toString() : null;
    }
}
