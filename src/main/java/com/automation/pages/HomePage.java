package com.automation.pages;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage extends BasePage {

    @iOSXCUITFindBy(iOSClassChain = "**/XCUIElementTypeIcon")
    private List<WebElement> appIcons;

    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeSearchField'")
    private WebElement spotlightSearch;

    @iOSXCUITFindBy(accessibility = "Settings")
    private WebElement settingsIcon;

    @iOSXCUITFindBy(accessibility = "Safari")
    private WebElement safariIcon;

    @Override
    public boolean isPageLoaded() {
        return !appIcons.isEmpty();
    }

    public HomePage openSpotlightSearch() {
        tap(spotlightSearch);
        return this;
    }

    public HomePage searchSpotlight(String query) {
        type(spotlightSearch, query);
        return this;
    }

    public void openSettings() {
        tap(settingsIcon);
    }

    public void openSafari() {
        tap(safariIcon);
    }

    public void openAppByName(String appName) {
        for (WebElement icon : appIcons) {
            if (appName.equals(icon.getAttribute("name"))) {
                tap(icon);
                return;
            }
        }
        throw new RuntimeException("App not found on home screen: " + appName);
    }

    public int getVisibleAppCount() {
        return appIcons.size();
    }
}
