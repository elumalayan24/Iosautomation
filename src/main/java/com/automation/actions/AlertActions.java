package com.automation.actions;

import com.automation.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;

public final class AlertActions {

    private static final Logger log = LogManager.getLogger(AlertActions.class);

    private AlertActions() {}

    public static void accept() {
        try {
            DriverManager.getDriver().switchTo().alert().accept();
            log.debug("Alert accepted");
        } catch (NoAlertPresentException e) {
            log.debug("No alert present to accept");
        }
    }

    public static void dismiss() {
        try {
            DriverManager.getDriver().switchTo().alert().dismiss();
            log.debug("Alert dismissed");
        } catch (NoAlertPresentException e) {
            log.debug("No alert present to dismiss");
        }
    }

    public static String getText() {
        try {
            return DriverManager.getDriver().switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            log.debug("No alert present to read text from");
            return null;
        }
    }

    public static boolean isPresent() {
        try {
            DriverManager.getDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static void acceptIfPresent() {
        if (isPresent()) {
            accept();
        }
    }

    public static void dismissIfPresent() {
        if (isPresent()) {
            dismiss();
        }
    }
}
