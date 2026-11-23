package com.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ConfigValidator {

    private static final Logger log = LogManager.getLogger(ConfigValidator.class);

    private static final String[] REQUIRED_KEYS = {
            "appium.serverUrl",
            "ios.platformName",
            "ios.automationName",
            "ios.platformVersion",
            "ios.deviceName",
            "ios.udid",
            "timeouts.implicitWait",
            "timeouts.explicitWait"
    };

    private ConfigValidator() {}

    public static void validate() {
        ConfigReader config = ConfigReader.getInstance();
        List<String> missing = new ArrayList<>();

        for (String key : REQUIRED_KEYS) {
            String value = config.getString(key);
            if (value == null || value.isBlank()) {
                missing.add(key);
            }
        }

        if (!missing.isEmpty()) {
            String message = "Missing required config keys: " + String.join(", ", missing);
            log.error(message);
            throw new IllegalStateException(message);
        }

        validateAppTarget(config);
        validateTimeouts(config);

        log.info("Configuration validated successfully");
    }

    private static void validateAppTarget(ConfigReader config) {
        String bundleId = config.getString("ios.bundleId");
        String app = config.getString("ios.app");

        boolean hasBundleId = bundleId != null && !bundleId.isBlank();
        boolean hasApp = app != null && !app.isBlank();

        if (!hasBundleId && !hasApp) {
            log.warn("Neither bundleId nor app path is set - driver will launch without an app target");
        }
    }

    private static void validateTimeouts(ConfigReader config) {
        int implicit = config.getInt("timeouts.implicitWait");
        int explicit = config.getInt("timeouts.explicitWait");

        if (implicit >= explicit) {
            log.warn("implicitWait ({}) should be less than explicitWait ({}) to avoid unpredictable behavior",
                    implicit, explicit);
        }

        if (implicit > 30) {
            log.warn("implicitWait of {}s is unusually high and will slow down element-not-found failures", implicit);
        }
    }
}
