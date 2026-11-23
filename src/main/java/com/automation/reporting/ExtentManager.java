package com.automation.reporting;

import com.automation.config.ConfigReader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public final class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ExtentManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    public static void setTest(ExtentTest test) {
        CURRENT_TEST.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT_TEST.get();
    }

    public static void removeTest() {
        CURRENT_TEST.remove();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    private static ExtentReports createInstance() {
        ConfigReader config = ConfigReader.getInstance();
        String reportPath = config.getString("reports.extentReportPath");

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("iOS Automation Report");
        spark.config().setReportName("Test Execution Results");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(spark);
        reports.setSystemInfo("Platform", "iOS");
        reports.setSystemInfo("Automation", "XCUITest");
        reports.setSystemInfo("Device", config.getString("ios.deviceName"));
        reports.setSystemInfo("OS Version", config.getString("ios.platformVersion"));

        return reports;
    }
}
