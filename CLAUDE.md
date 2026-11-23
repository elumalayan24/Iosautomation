# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Compile
mvn compile

# Compile including test classes
mvn test-compile

# Run all tests (uses testng.xml at project root)
mvn test -DsuiteFile=testng.xml

# Run by profile
mvn test -Psmoke          # smoke suite only
mvn test -Pregression     # full regression

# Run a single test class
mvn test -DsuiteFile=testng.xml -Dtest=com.automation.tests.SettingsTest

# Clean build
mvn clean compile test-compile
```

## Prerequisites Before Running Tests

1. iOS simulator must be booted (check: `xcrun simctl list devices | grep Booted`)
2. Appium server must be running: `appium`
3. `config.yaml` must have a valid `bundleId` or `app` path set under `ios:`

## Selenium BOM Pin — Do Not Remove

Appium java-client 9.3.0 transitively resolves Selenium 4.54.0, which removed `ContextAware` and `LocationContext` interfaces that the Appium client hierarchy still references. The `selenium-bom` in `dependencyManagement` pins Selenium to 4.25.0 to fix this. Removing the BOM or bumping past 4.25.x will break compilation.

## Architecture

**Driver lifecycle** is ThreadLocal-based for parallel safety:
- `DriverManager` holds `ThreadLocal<IOSDriver>` — the single access point for the current thread's driver
- `DriverFactory` orchestrates creation: validates config → builds options via `DriverOptionsFactory` → connects to Appium → stores in `DriverManager`
- `DriverOptionsFactory` builds `XCUITestOptions` from `config.yaml` and is the only place XCUITest capabilities are set

**Page Object Model** — all page classes extend `BasePage`, which:
- Pulls the driver from `DriverManager.getDriver()` (not passed as a constructor arg)
- Initializes Appium's `PageFactory` with `AppiumFieldDecorator` in the constructor
- Provides `tap()`, `type()`, `getText()`, `isDisplayed()` wrappers with built-in explicit waits
- Every page must implement `isPageLoaded()` for verification

**Config** is read from `src/main/resources/config.yaml` via `ConfigReader` (double-checked locking singleton). Access values with dot-path notation: `ConfigReader.getInstance().getString("ios.deviceName")`. `ConfigValidator` runs automatically on driver creation.

**Actions are split by concern:**
- `IOSActions` — app lifecycle (background, activate, terminate, clipboard, lock/unlock)
- `GestureActions` — touch gestures via W3C Actions API (swipe, scroll, long press, double tap, drag)
- `AlertActions` — native alert handling (accept, dismiss, getText)

**Reporting** runs via two TestNG listeners registered on `BaseTest`:
- `ExtentReportListener` → `ExtentManager` → HTML report at `target/reports/extent-report.html`
- `TestListener` logs pass/fail/skip and captures screenshots on failure
- Allure is wired via aspectjweaver in surefire; results go to `target/allure-results`

**Test classes** extend `com.automation.base.BaseTest`, which handles `@BeforeMethod` driver creation and `@AfterMethod` teardown. Page objects are instantiated in test-class-level `@BeforeMethod`.

## Conventions

- Element locators use `@iOSXCUITFindBy` annotations (accessibility, iOSClassChain, iOSNsPredicate) — not `@FindBy`
- Page methods return `this` for chaining (fluent pattern)
- Assertions use AssertJ (`assertThat`), not TestNG asserts
- All action/util classes are `final` with private constructors (static-only)
- Suite XML files live in `src/test/resources/suites/`; the root `testng.xml` runs everything
