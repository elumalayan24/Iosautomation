package com.automation.pages;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class SettingsPage extends BasePage {

    @iOSXCUITFindBy(accessibility = "Settings")
    private WebElement settingsHeader;

    @iOSXCUITFindBy(accessibility = "General")
    private WebElement generalOption;

    @iOSXCUITFindBy(accessibility = "About")
    private WebElement aboutOption;

    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeNavigationBar[`name == \"Settings\"`]")
    private WebElement navigationBar;

    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeSearchField'")
    private WebElement searchField;

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(navigationBar);
    }

    public SettingsPage tapGeneral() {
        tap(generalOption);
        return this;
    }

    public SettingsPage tapAbout() {
        tap(aboutOption);
        return this;
    }

    public SettingsPage search(String query) {
        type(searchField, query);
        return this;
    }

    public String getHeaderText() {
        return getText(settingsHeader);
    }
}
