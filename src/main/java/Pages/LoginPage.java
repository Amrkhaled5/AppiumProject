package Pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import java.util.Objects;
import base.BaseClass;

public class LoginPage {
    private final BaseClass base;

    public LoginPage(AppiumDriver driver){
        if (driver == null) {
            throw new IllegalArgumentException("LoginPage constructor received null driver. Make sure driver is initialized before creating page objects.");
        }
        this.base = new BaseClass();
        this.base.setDriver(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    private final By UsernameInput = AppiumBy.accessibilityId("test-Username1");
    private final By PasswordInput = AppiumBy.accessibilityId("test-Password");
    private final By LoginButton = AppiumBy.accessibilityId("test-LOGIN");
    private final By ErrorMessageBox = AppiumBy.accessibilityId("test-Error message");
    private final By ErrorMessage = AppiumBy.xpath("//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]");

    public void enterUserName(String username) {
        base.sendKeys(UsernameInput, username);
    }

    public void enterPassword(String password) {
        base.sendKeys(PasswordInput, password);
    }

    public ProductsPage clickLoginButton() {
        base.click(LoginButton);
        return new ProductsPage(this.base.getDriver());
    }

    public boolean isErrorMessageDisplayed() {
        return base.isElementDisplayed(ErrorMessageBox);
    }

    public boolean isErrorMessageDisplayedCorrectError(String msg){
        String errorMsgText = base.getAttribute(ErrorMessage, "text");
        return Objects.equals(errorMsgText, msg);
    }


}