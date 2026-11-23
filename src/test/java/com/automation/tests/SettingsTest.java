package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SettingsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsTest extends BaseTest {

    private SettingsPage settingsPage;

    @BeforeMethod
    public void initPages() {
        settingsPage = new SettingsPage();
    }

    @Test(description = "Verify Settings page loads successfully")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Opens the Settings app and verifies the main page is loaded")
    public void verifySettingsPageLoads() {
        assertThat(settingsPage.isPageLoaded())
                .as("Settings page should be loaded")
                .isTrue();
    }

    @Test(description = "Navigate to General settings")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Navigates from Settings to General and verifies navigation")
    public void navigateToGeneral() {
        settingsPage.tapGeneral();
        log.info("Navigated to General settings");
    }

    @Test(description = "Navigate to About section")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigates from Settings to General > About")
    public void navigateToAbout() {
        settingsPage.tapGeneral();
        settingsPage.tapAbout();
        log.info("Navigated to About section");
    }

    @Test(description = "Search within Settings")
    @Severity(SeverityLevel.NORMAL)
    @Description("Uses the search field in Settings to find a setting")
    public void searchInSettings() {
        settingsPage.search("WiFi");
        log.info("Searched for WiFi in Settings");
    }
}
