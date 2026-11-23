package com.automation.reporting;

import com.automation.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public final class AllureManager {

    private static final Logger log = LogManager.getLogger(AllureManager.class);

    private AllureManager() {}

    public static void attachScreenshot(String name) {
        byte[] screenshot = ScreenshotUtil.captureAsBytes();
        if (screenshot.length > 0) {
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
            log.debug("Screenshot attached to Allure: {}", name);
        }
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

    public static void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json", json);
    }

    public static void addStep(String stepName) {
        Allure.step(stepName);
    }

    public static void addStep(String stepName, Status status) {
        String uuid = UUID.randomUUID().toString();
        StepResult step = new StepResult().setName(stepName).setStatus(status);
        Allure.getLifecycle().startStep(uuid, step);
        Allure.getLifecycle().stopStep(uuid);
    }

    public static void addEnvironmentInfo(String key, String value) {
        Allure.parameter(key, value);
    }

    public static void addDescription(String description) {
        Allure.description(description);
    }

    public static void addLink(String name, String url) {
        Allure.link(name, url);
    }

    public static void addIssue(String issueId) {
        Allure.issue(issueId, issueId);
    }
}
