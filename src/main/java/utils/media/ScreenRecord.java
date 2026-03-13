package utils.media;

import core.DriverManager;
import io.appium.java_client.screenrecording.CanRecordScreen;
import logs.LogsManager;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ScreenRecord {

    public static final String VIDEO_PATH = "test-results/videos/";

    public static void startRecording() {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (driver instanceof CanRecordScreen) {
                ((CanRecordScreen) driver).startRecordingScreen();
                LogsManager.info("Screen recording started.");
            }
        } catch (Exception e) {
            LogsManager.error("Failed to start screen recording: " + e.getMessage());
        }
    }

    public static void stopRecording(String testName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (driver instanceof CanRecordScreen) {
                String base64Video = ((CanRecordScreen) driver).stopRecordingScreen();

                File dir = new File(VIDEO_PATH);
                if (!dir.exists()) dir.mkdirs();

                File videoFile = new File(dir, testName + ".mp4");

                try (FileOutputStream stream = new FileOutputStream(videoFile)) {
                    stream.write(Base64.getDecoder().decode(base64Video));
                    LogsManager.info("Screen recording saved to MP4: " + videoFile.getAbsolutePath());

                } catch (IOException e) {
                    LogsManager.error("Failed to save video file: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            LogsManager.error("Failed to stop screen recording: " + e.getMessage());
        }
    }

    public static void stopAndDiscardRecording() {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (driver instanceof CanRecordScreen) {
                ((CanRecordScreen) driver).stopRecordingScreen();
                LogsManager.info("Screen recording discarded for passed/skipped test to save space.");
            }
        } catch (Exception e) {
            LogsManager.error("Failed to discard screen recording: " + e.getMessage());
        }
    }
}