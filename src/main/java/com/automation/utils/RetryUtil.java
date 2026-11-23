package com.automation.utils;

import com.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class RetryUtil {

    private static final Logger log = LogManager.getLogger(RetryUtil.class);

    private RetryUtil() {}

    public static <T> T execute(Supplier<T> action, String actionDescription) {
        int maxAttempts = ConfigReader.getInstance().getInt("retry.maxAttempts");
        int delay = ConfigReader.getInstance().getInt("retry.delayBetweenRetries");
        return execute(action, maxAttempts, delay, actionDescription);
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts, int delaySeconds, String actionDescription) {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return action.get();
            } catch (Exception e) {
                lastException = e;
                log.warn("Attempt {}/{} failed for '{}': {}", attempt, maxAttempts, actionDescription, e.getMessage());

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delaySeconds * 1000L);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }

        throw new RuntimeException(
                "All " + maxAttempts + " attempts failed for '" + actionDescription + "'", lastException);
    }

    public static void executeVoid(Runnable action, String actionDescription) {
        execute(() -> {
            action.run();
            return null;
        }, actionDescription);
    }
}
