package com.automation.utils;

import com.automation.driver.DriverManager;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String SCREENSHOT_DIR = "target/screenshots";

    private ScreenshotUtil() {}

    public static String capture(String name) {
        if (!DriverManager.isDriverActive()) {
            log.warn("Cannot capture screenshot - driver is not active");
            return null;
        }

        try {
            byte[] screenshotBytes = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);

            String fileName = name + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
            Path directory = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(directory);
            Path filePath = directory.resolve(fileName);
            Files.write(filePath, screenshotBytes);

            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshotBytes), ".png");

            log.debug("Screenshot saved: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static byte[] captureAsBytes() {
        if (!DriverManager.isDriverActive()) {
            return new byte[0];
        }
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static String captureAsBase64() {
        if (!DriverManager.isDriverActive()) {
            return "";
        }
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }
}
