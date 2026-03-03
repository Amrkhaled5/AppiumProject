package CustomListners;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static logs.LogsManager.logger;

public class TestNGListeners implements ITestListener, IInvokedMethodListener, IExecutionListener {

    private String getStatusName(int status) {
        switch (status) {
            case 1: return "SUCCESS";
            case 2: return "FAILURE";
            case 3: return "SKIP";
            default: return "UNKNOWN";
        }
    }

    public void onTestSuccess(ITestResult result) {
        logger().info("Test Successfully Passed: " + result.getName());
    }

    // Helper: try multiple strategies to extract a WebDriver from the test instance
    private WebDriver extractWebDriver(ITestResult result) {
        if (result == null) return null;
        Object currentClass = result.getInstance();
        if (currentClass == null) return null;

        // 1) Try a public getDriver() method on the test class
        try {
            Method m = currentClass.getClass().getMethod("getDriver");
            Object drv = m.invoke(currentClass);
            if (drv instanceof WebDriver) return (WebDriver) drv;
        } catch (NoSuchMethodException ignored) {
            // continue to field search
        } catch (Exception e) {
            logger().error("Error invoking getDriver() on test class: " + e.getMessage());
        }

        // 2) Try commonly named fields on the test class (baseClass, base, driver)
        String[] candidateFields = {"baseClass", "base", "driver"};
        for (String fieldName : candidateFields) {
            try {
                Field f = currentClass.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                Object value = f.get(currentClass);
                if (value == null) continue;

                // if the field itself is a WebDriver
                if (value instanceof WebDriver) return (WebDriver) value;

                // if the field has a getDriver() method, invoke it
                try {
                    Method nestedGetDriver = value.getClass().getMethod("getDriver");
                    Object nestedDriver = nestedGetDriver.invoke(value);
                    if (nestedDriver instanceof WebDriver) return (WebDriver) nestedDriver;
                } catch (NoSuchMethodException ignored) {
                    // no getDriver on this field type
                }

            } catch (NoSuchFieldException ignored) {
                // try next field
            } catch (Exception e) {
                logger().error("Error reading field '" + fieldName + "' from test class: " + e.getMessage());
            }
        }

        // 3) Give up
        return null;
    }

    public void onTestFailure(ITestResult result) {
        logger().error("!!! Test Failed: " + result.getName());
        logger().error("Failure Reason: " + result.getThrowable());

        WebDriver driver = extractWebDriver(result);

        if (driver == null) {
            logger().warn("WebDriver instance not found for test '" + result.getName() + "' — cannot capture screenshot.");
            return;
        }

        if (!(driver instanceof TakesScreenshot)) {
            logger().warn("Found WebDriver is not a TakesScreenshot implementation — skipping screenshot.");
            return;
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Ensure screenshots directory exists
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            boolean created = screenshotsDir.mkdirs();
            if (!created) {
                logger().error("Failed to create screenshots directory: " + screenshotsDir.getAbsolutePath());
            }
        }

        String screenshotName = result.getName() + ".png";
        File destFile = new File(screenshotsDir, screenshotName);

        try {
            FileUtils.copyFile(srcFile, destFile);
            logger().info("Screenshot saved to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            logger().error("Failed to save screenshot: " + e.getMessage());
        }
    }

    public void onTestSkipped(ITestResult result) {
        logger().warn("Test Skipped: " + result.getName());    }
    ///////////////////////////////////////////////////////////////////////////////
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        logger().debug("Invoking method: " + method.getTestMethod().getMethodName()
                + " inside class: " + testResult.getTestClass().getName());    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        String status = getStatusName(testResult.getStatus());

        logger().debug("Finished method: " + method.getTestMethod().getMethodName()
                + " | Status: " + status);    }
    ///////////////////////////////////////////////////////////////////////////////
    public void onExecutionStart() {
        logger().info(" Execution Started ");    }

    public void onExecutionFinish() {
        logger().info(" Execution finished ");

    }


}