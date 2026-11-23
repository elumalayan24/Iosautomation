package com.automation.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public final class AppiumServerManager {

    private static final Logger log = LogManager.getLogger(AppiumServerManager.class);
    private static Process appiumProcess;

    private AppiumServerManager() {}

    public static void startServer(String host, int port) {
        if (isServerRunning(host, port)) {
            log.info("Appium server already running on {}:{}", host, port);
            return;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder("appium",
                    "--address", host,
                    "--port", String.valueOf(port),
                    "--relaxed-security",
                    "--log-level", "warn");
            builder.redirectErrorStream(true);
            appiumProcess = builder.start();

            waitForServerReady(host, port, 30);
            log.info("Appium server started on {}:{}", host, port);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start Appium server", e);
        }
    }

    public static void stopServer() {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            appiumProcess.destroyForcibly();
            log.info("Appium server stopped");
        }
    }

    public static boolean isServerRunning(String host, int port) {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                    URI.create("http://" + host + ":" + port + "/status").toURL().openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static void waitForServerReady(String host, int port, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            if (isServerRunning(host, port)) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for Appium server");
            }
        }
        throw new IllegalStateException("Appium server did not start within " + timeoutSeconds + " seconds");
    }
}
