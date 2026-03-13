package utils.media;

import core.DriverManager;
import logs.LogsManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;

public class ScreenShots {

    public static final String SCREENSHOT_PATH = "test-results/screenshots/";

    public static void takeFullPageScreenShot(String screenShotName) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return;

        try {
            File screenshotSrc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            File dir = new File(SCREENSHOT_PATH);
            if (!dir.exists()) dir.mkdirs();

            File screenshotDest = new File(dir, screenShotName + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotDest);

            LogsManager.info("Screenshot taken: " + screenshotDest.getAbsolutePath());

        } catch (IOException e) {
            LogsManager.error("Failed to take screenshot: " + e.getMessage());
        }
    }

    public static void takeScreenShotForElement(WebElement element, String screenShotName) {
        try {
            File screenshotSrc = element.getScreenshotAs(OutputType.FILE);

            File dir = new File(SCREENSHOT_PATH);
            if (!dir.exists()) dir.mkdirs();

            File screenshotDest = new File(dir, screenShotName + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotDest);

            LogsManager.info("Element screenshot taken: " + screenshotDest.getAbsolutePath());
        } catch (IOException e) {
            LogsManager.error("Failed to take element screenshot: " + e.getMessage());
        }
    }
}