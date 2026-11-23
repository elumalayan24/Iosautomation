package com.automation.pages;

import com.automation.config.ConfigReader;
import com.automation.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final IOSDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        int explicitWait = ConfigReader.getInstance().getInt("timeouts.explicitWait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(explicitWait)), this);
    }

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void tap(WebElement element) {
        waitForClickable(element).click();
        log.debug("Tapped on element: {}", element);
    }

    protected void type(WebElement element, String text) {
        WebElement visible = waitForVisible(element);
        visible.clear();
        visible.sendKeys(text);
        log.debug("Typed '{}' into element: {}", text, element);
    }

    protected String getText(WebElement element) {
        return waitForVisible(element).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(WebElement element) {
        try {
            return waitForVisible(element).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public abstract boolean isPageLoaded();
}
