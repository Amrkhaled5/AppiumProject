package base;

import core.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

public class BaseTest {

    @Parameters({"platformName", "deviceName"})
    @BeforeClass
    public void setUp(String platformName, String deviceName) throws Exception {
        Properties props = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new RuntimeException("Property file 'config.properties' not found");
            }
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, platformName);
        caps.setCapability("appium:deviceName", deviceName);
        caps.setCapability("appium:automationName", props.getProperty("androidAutomationName"));
        caps.setCapability("appium:udid", "emulator-5554");
        caps.setCapability("appium:newCommandTimeout", 300);
        caps.setCapability("appium:appWaitActivity", "com.swaglabsmobileapp.MainActivity");
        caps.setCapability("appium:appWaitDuration", 40000);

        String appPathFromConfig = props.getProperty("androidAppLocation");
        appPathFromConfig = appPathFromConfig.replace("/", File.separator).replace("\\", File.separator);
        String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + appPathFromConfig;

        caps.setCapability("appium:app", appUrl);

        URL url = new URL(props.getProperty("androidURL"));
        AndroidDriver driver = new AndroidDriver(url, caps);

        DriverManager.setDriver(driver);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}