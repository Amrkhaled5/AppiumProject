package listeners;

import logs.LogsManager;
import org.testng.*;
import utils.media.ScreenRecord;
import utils.media.ScreenShots;

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
    public void onTestStart(ITestResult result) {
        // Start the video via utility class
        ScreenRecord.startRecording();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogsManager.info("Test Successfully Passed: " + result.getName());
        // Dump the video to save hard drive space
        ScreenRecord.stopAndDiscardRecording();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogsManager.error("!!! Test Failed: " + result.getName());
        LogsManager.error("Failure Reason: " + result.getThrowable());

        // Call your dedicated media utilities
        ScreenShots.takeFullPageScreenShot(result.getName());
        ScreenRecord.stopRecording(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogsManager.warn("Test Skipped: " + result.getName());
        ScreenRecord.stopAndDiscardRecording();
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