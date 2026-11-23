package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    public void initPages() {
        homePage = new HomePage();
    }

    @Test(description = "Verify home screen loads with app icons")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies the home screen is loaded and app icons are visible")
    public void verifyHomeScreenLoads() {
        assertThat(homePage.isPageLoaded())
                .as("Home screen should display app icons")
                .isTrue();
    }

    @Test(description = "Verify app icons are present on home screen")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Counts visible app icons on the home screen")
    public void verifyAppIconsVisible() {
        int appCount = homePage.getVisibleAppCount();
        assertThat(appCount)
                .as("Home screen should have at least one app icon")
                .isGreaterThan(0);
        log.info("Found {} app icons on home screen", appCount);
    }

    @Test(description = "Open Settings from home screen")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Taps Settings icon on home screen and navigates to Settings app")
    public void openSettingsFromHome() {
        homePage.openSettings();
        log.info("Opened Settings from home screen");
    }
}
