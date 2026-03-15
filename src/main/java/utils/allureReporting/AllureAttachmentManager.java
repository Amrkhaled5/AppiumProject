package utils.allureReporting;

import io.qameta.allure.Allure;
import logs.LogsManager;
import utils.media.ScreenRecord;
import utils.media.ScreenShots;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllureAttachmentManager {

    public static void attachScreenshot(String testName) {
        try {
            Path screenshotPath = Path.of(ScreenShots.SCREENSHOT_PATH + testName + ".png");
            if (Files.exists(screenshotPath)) {
                Allure.addAttachment("Failed Test Screenshot", Files.newInputStream(screenshotPath));
            } else {
                LogsManager.warn("Screenshot file does not exist to attach: " + screenshotPath);
            }
        } catch (Exception e) {
            LogsManager.error("Failed to attach screenshot: " + e.getMessage());
        }
    }

    public static void attachVideo(String testName) {
        try {
            File record = new File(ScreenRecord.VIDEO_PATH + testName + ".mp4");
            if (record.exists()) {
                Allure.addAttachment("Test Execution Video", "video/mp4", Files.newInputStream(record.toPath()), ".mp4");
            }
        } catch (Exception e) {
            LogsManager.error("Failed to attach video record: " + e.getMessage());
        }
    }
}