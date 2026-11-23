package com.automation.actions;

import com.automation.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

public final class GestureActions {

    private static final Logger log = LogManager.getLogger(GestureActions.class);
    private static final PointerInput FINGER = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    private GestureActions() {}

    public static void swipeUp() {
        swipe(Direction.UP, 0.75, 0.25);
    }

    public static void swipeDown() {
        swipe(Direction.DOWN, 0.25, 0.75);
    }

    public static void swipeLeft() {
        swipe(Direction.LEFT, 0.75, 0.25);
    }

    public static void swipeRight() {
        swipe(Direction.RIGHT, 0.25, 0.75);
    }

    public static void swipe(Direction direction, double startPercent, double endPercent) {
        Dimension size = DriverManager.getDriver().manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int centerY = size.getHeight() / 2;

        Point start = new Point(centerX, centerY);
        Point end = new Point(centerX, centerY);

        switch (direction) {
            case UP -> {
                start = new Point(centerX, (int) (size.getHeight() * startPercent));
                end = new Point(centerX, (int) (size.getHeight() * endPercent));
            }
            case DOWN -> {
                start = new Point(centerX, (int) (size.getHeight() * startPercent));
                end = new Point(centerX, (int) (size.getHeight() * endPercent));
            }
            case LEFT -> {
                start = new Point((int) (size.getWidth() * startPercent), centerY);
                end = new Point((int) (size.getWidth() * endPercent), centerY);
            }
            case RIGHT -> {
                start = new Point((int) (size.getWidth() * startPercent), centerY);
                end = new Point((int) (size.getWidth() * endPercent), centerY);
            }
        }

        performSwipe(start, end, Duration.ofMillis(300));
        log.debug("Swiped {} from {} to {}", direction, start, end);
    }

    public static void scrollToElement(WebElement element, Direction direction, int maxScrolls) {
        for (int i = 0; i < maxScrolls; i++) {
            try {
                if (element.isDisplayed()) {
                    log.debug("Element found after {} scrolls", i);
                    return;
                }
            } catch (Exception ignored) {}
            swipe(direction, 0.7, 0.3);
        }
        throw new RuntimeException("Element not found after " + maxScrolls + " scrolls");
    }

    public static void longPress(WebElement element, Duration duration) {
        Point center = getElementCenter(element);

        Sequence longPress = new Sequence(FINGER, 0);
        longPress.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.getX(), center.getY()));
        longPress.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(new Pause(FINGER, duration));
        longPress.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        DriverManager.getDriver().perform(List.of(longPress));
        log.debug("Long pressed at ({}, {}) for {}ms", center.getX(), center.getY(), duration.toMillis());
    }

    public static void doubleTap(WebElement element) {
        Point center = getElementCenter(element);

        Sequence doubleTap = new Sequence(FINGER, 0);
        doubleTap.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.getX(), center.getY()));
        doubleTap.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(new Pause(FINGER, Duration.ofMillis(50)));
        doubleTap.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(new Pause(FINGER, Duration.ofMillis(100)));
        doubleTap.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(new Pause(FINGER, Duration.ofMillis(50)));
        doubleTap.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        DriverManager.getDriver().perform(List.of(doubleTap));
        log.debug("Double tapped at ({}, {})", center.getX(), center.getY());
    }

    public static void tapAtCoordinates(int x, int y) {
        Sequence tap = new Sequence(FINGER, 0);
        tap.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(FINGER, Duration.ofMillis(50)));
        tap.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        DriverManager.getDriver().perform(List.of(tap));
        log.debug("Tapped at ({}, {})", x, y);
    }

    public static void dragAndDrop(WebElement source, WebElement target) {
        Point from = getElementCenter(source);
        Point to = getElementCenter(target);

        Sequence drag = new Sequence(FINGER, 0);
        drag.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), from.getX(), from.getY()));
        drag.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        drag.addAction(new Pause(FINGER, Duration.ofMillis(500)));
        drag.addAction(FINGER.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), to.getX(), to.getY()));
        drag.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        DriverManager.getDriver().perform(List.of(drag));
        log.debug("Dragged from ({},{}) to ({},{})", from.getX(), from.getY(), to.getX(), to.getY());
    }

    private static void performSwipe(Point start, Point end, Duration duration) {
        Sequence swipe = new Sequence(FINGER, 0);
        swipe.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(FINGER.createPointerMove(duration, PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        DriverManager.getDriver().perform(List.of(swipe));
    }

    private static Point getElementCenter(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
