package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BaseClass {
    protected AppiumDriver driver;
    protected Properties props;
    InputStream inputStream;

    private static final String DEVICE_NAME = "appium:deviceName";
    private static final String AUTOMATION_NAME = "appium:automationName";
    private static final String UDID = "appium:udid";
    private static final String APP = "appium:app";
    private static final String NEWCOMMANDTIMEOUT = "appium:newCommandTimeout";
    private static final String APP_WAIT_ACTIVITY = "appium:appWaitActivity";
    private static final String APP_WAIT_DURATION = "appium:appWaitDuration";

    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
    }

    public AppiumDriver getDriver() {
        return driver;
    }


    public void setUpDriver(String platformName,String deviceName) throws IOException {
        try {
            props = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new RuntimeException("Property file '" + propFileName + "' not found in the classpath");
            }

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(PLATFORM_NAME, platformName);
            caps.setCapability(DEVICE_NAME, deviceName);
            URL url = new URL(props.getProperty("androidURL"));
            caps.setCapability(AUTOMATION_NAME, props.getProperty("androidAutomationName"));
            caps.setCapability(UDID, "emulator-5554");
            caps.setCapability(NEWCOMMANDTIMEOUT, 300);
            caps.setCapability(APP_WAIT_ACTIVITY, "*");
            caps.setCapability(APP_WAIT_DURATION, 40000);

            String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                    + File.separator + "resources" + File.separator + props.getProperty("androidAppLocation");
            caps.setCapability(APP, appUrl);

            driver = new AndroidDriver(url, caps);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void waitForVisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.EXPLICIT_WAIT));
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

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

}

