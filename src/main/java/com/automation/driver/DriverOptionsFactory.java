package com.automation.driver;

import com.automation.config.ConfigReader;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.time.Duration;

public final class DriverOptionsFactory {

    private DriverOptionsFactory() {}

    public static XCUITestOptions createIOSOptions() {
        ConfigReader config = ConfigReader.getInstance();
        return buildFromConfig(config);
    }

    public static XCUITestOptions createIOSOptions(String appPath) {
        XCUITestOptions options = createIOSOptions();
        options.setApp(appPath);
        return options;
    }

    public static XCUITestOptions createIOSOptionsForBundleId(String bundleId) {
        XCUITestOptions options = createIOSOptions();
        options.setBundleId(bundleId);
        return options;
    }

    private static XCUITestOptions buildFromConfig(ConfigReader config) {
        XCUITestOptions options = new XCUITestOptions();

        options.setPlatformName(config.getString("ios.platformName"));
        options.setAutomationName(config.getString("ios.automationName"));
        options.setPlatformVersion(config.getString("ios.platformVersion"));
        options.setDeviceName(config.getString("ios.deviceName"));
        options.setUdid(config.getString("ios.udid"));

        String bundleId = config.getString("ios.bundleId");
        if (bundleId != null && !bundleId.isEmpty()) {
            options.setBundleId(bundleId);
        }

        String app = config.getString("ios.app");
        if (app != null && !app.isEmpty()) {
            options.setApp(app);
        }

        options.setNoReset(config.getBoolean("ios.noReset"));
        options.setFullReset(config.getBoolean("ios.fullReset"));
        options.setNewCommandTimeout(Duration.ofSeconds(config.getInt("appium.newCommandTimeout")));
        options.setWdaLocalPort(config.getInt("ios.wdaLocalPort"));
        options.setUseNewWDA(config.getBoolean("ios.useNewWDA"));
        options.setShowXcodeLog(config.getBoolean("ios.showXcodeLog"));
        options.setCapability("waitForQuiescence", config.getBoolean("ios.waitForQuiescence"));
        options.setCapability("maxTypingFrequency", config.getInt("ios.maxTypingFrequency"));

        return options;
    }
}
