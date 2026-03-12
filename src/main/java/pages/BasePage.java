package pages;

import core.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;
import java.util.Collections;

public class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.EXPLICIT_WAIT));
    }

    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void sendKeys(By locator, String text) {
        waitForVisibility(locator);
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    public void click(By locator) {
        waitForVisibility(locator);
        driver.findElement(locator).click();
    }

    public String getAttribute(By locator, String attribute) {
        waitForVisibility(locator);
        return driver.findElement(locator).getAttribute(attribute);
    }

    public boolean isElementDisplayed(By locator) {
        try {
            waitForVisibility(locator);
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void swipeDown() {
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    public void scrollToElement(By locator) {
        int maxSwipes = 5;

        for (int i = 0; i < maxSwipes; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                swipeDown();
            }
        }
        throw new RuntimeException("Element not found after " + maxSwipes + " swipes: " + locator.toString());
    }

}