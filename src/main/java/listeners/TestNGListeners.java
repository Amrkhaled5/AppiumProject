package listeners;

import core.DriverManager;
import logs.LogsManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;

public class TestNGListeners implements ITestListener, IInvokedMethodListener, IExecutionListener {

    private String getStatusName(int status) {
        switch (status) {
            case 1: return "SUCCESS";
            case 2: return "FAILURE";
            case 3: return "SKIP";
            default: return "UNKNOWN";
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogsManager.info("Test Successfully Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsManager.error("!!! Test Failed: " + result.getName());
        LogsManager.error("Failure Reason: " + result.getThrowable());

        // Simple, clean driver retrieval using ThreadLocal Manager
        WebDriver driver = DriverManager.getDriver();

        if (driver == null) {
            LogsManager.warn("WebDriver instance not found — cannot capture screenshot.");
            return;
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenshotsDir = new File("screenshots");

        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }

        File destFile = new File(screenshotsDir, result.getName() + ".png");

        try {
            FileUtils.copyFile(srcFile, destFile);
            LogsManager.info("Screenshot saved to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            LogsManager.error("Failed to save screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsManager.warn("Test Skipped: " + result.getName());
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        LogsManager.debug("Invoking method: " + method.getTestMethod().getMethodName()
                + " inside class: " + testResult.getTestClass().getName());
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        String status = getStatusName(testResult.getStatus());
        LogsManager.debug("Finished method: " + method.getTestMethod().getMethodName()
                + " | Status: " + status);
    }

    @Override
    public void onExecutionStart() {
        LogsManager.info("Execution Started");
    }

    @Override
    public void onExecutionFinish() {
        LogsManager.info("Execution finished");
    }
}