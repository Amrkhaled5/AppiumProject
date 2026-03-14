package base;
import core.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utils.datamanager.PropertyReader;
import java.io.File;
import java.net.URL;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;


public class BaseTest {

    @Parameters({"platformName", "deviceName", "udid", "systemPort"})
    @BeforeClass
    public void setUp(String platformName, String deviceName, String udid, String systemPort) throws Exception {

        ThreadContext.put("device", deviceName);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, platformName);
        caps.setCapability("appium:deviceName", deviceName);
        caps.setCapability("appium:udid", udid);

        URL url = new URL(PropertyReader.getProperty("androidURL"));
        AppiumDriver driver;

        switch (platformName.toLowerCase()) {
            case "android":
                caps.setCapability("appium:automationName", PropertyReader.getProperty("androidAutomationName"));
                caps.setCapability("appium:newCommandTimeout", 300);
                caps.setCapability("appium:appWaitActivity", "com.swaglabsmobileapp.MainActivity");
                caps.setCapability("appium:appWaitDuration", 40000);
                caps.setCapability("appium:systemPort", Integer.parseInt(systemPort));

                String appPathFromConfig = PropertyReader.getProperty("androidAppLocation");
                appPathFromConfig = appPathFromConfig.replace("/", File.separator).replace("\\", File.separator);
                String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "resources" + File.separator + appPathFromConfig;

                caps.setCapability("appium:app", appUrl);

                driver = new AndroidDriver(url, caps);
                break;
            case "ios":
                caps.setCapability("appium:automationName", PropertyReader.getProperty("iosAutomationName"));
                caps.setCapability("appium:bundleId", PropertyReader.getProperty("iosBundleId"));

                String iosAppPath = PropertyReader.getProperty("iosAppLocation");
                iosAppPath = iosAppPath.replace("/", File.separator).replace("\\", File.separator);
                String iosAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                        + File.separator + "resources" + File.separator + iosAppPath;

                caps.setCapability("appium:app", iosAppUrl);

                driver = new IOSDriver(url, caps);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
        }
        DriverManager.setDriver(driver);
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
        ThreadContext.clearAll();
    }
}