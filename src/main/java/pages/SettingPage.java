package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class SettingPage extends BasePage{
    private final By logoutButton= AppiumBy.accessibilityId("test-LOGOUT");

    public LoginPage clickLogoutButton(){
        click(logoutButton);
        return new LoginPage();
    }
}
