package base;

import core.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utils.datamanager.PropertyReader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BaseTest {

    @Parameters({"platformName", "deviceName"})
    @BeforeClass
    public void setUp(String platformName, String deviceName) throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, platformName);
        caps.setCapability("appium:deviceName", deviceName);

        // Fetch properties dynamically using PropertyReader
        caps.setCapability("appium:automationName", PropertyReader.getProperty("androidAutomationName"));
        caps.setCapability("appium:udid", "emulator-5554");
        caps.setCapability("appium:newCommandTimeout", 300);
        caps.setCapability("appium:appWaitActivity", "com.swaglabsmobileapp.MainActivity");
        caps.setCapability("appium:appWaitDuration", 40000);

        String appPathFromConfig = PropertyReader.getProperty("androidAppLocation");
        appPathFromConfig = appPathFromConfig.replace("/", File.separator).replace("\\", File.separator);
        String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + appPathFromConfig;

        caps.setCapability("appium:app", appUrl);
        URL url = new URL(PropertyReader.getProperty("androidURL"));
        AndroidDriver driver = new AndroidDriver(url, caps);

        DriverManager.setDriver(driver);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}