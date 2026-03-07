package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import java.util.Objects;

public class LoginPage extends BasePage {

    // Standardized to camelCase variables
    private final By usernameInput = AppiumBy.accessibilityId("test-Username");
    private final By passwordInput = AppiumBy.accessibilityId("test-Password");
    private final By loginButton = AppiumBy.accessibilityId("test-LOGIN");
    private final By errorMessageBox = AppiumBy.accessibilityId("test-Error message");
    private final By errorMessageText = AppiumBy.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]");

    public void enterUserName(String username) {
        sendKeys(usernameInput, username);
    }

    public void enterPassword(String password) {
        sendKeys(passwordInput, password);
    }

    public ProductsPage clickLoginButton() {
        click(loginButton);
        return new ProductsPage(); // Smooth transition to the next POM
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessageBox);
    }

    public boolean isErrorMessageDisplayedCorrectError(String msg) {
        String actualErrorMsg = getAttribute(errorMessageText, "text");
        return Objects.equals(actualErrorMsg, msg);
    }
}