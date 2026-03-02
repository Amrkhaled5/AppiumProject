package Pages;

import base.BaseClass;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;

public class ProductsPage {
    BaseClass base;
    By productTitle = By.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]");

    public ProductsPage(AppiumDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("ProductsPage constructor received null driver. Make sure driver is initialized before creating page objects.");
        }
        base = new BaseClass();
        base.setDriver(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isProductsPageDisplayed() {
        return base.isElementDisplayed(productTitle);
    }

}