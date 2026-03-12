package pages;

import io.appium.java_client.AppiumBy;
import net.bytebuddy.implementation.bytecode.ShiftRight;
import org.openqa.selenium.By;

public class ProductsPage extends BasePage {
    private final By productTitle = AppiumBy.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]");
    private final By OnesieTitle = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Onesie\"]");

    public boolean isProductsPageDisplayed() {
        return isElementDisplayed(productTitle);
    }

    public String getProductTitle() {
        return getAttribute(OnesieTitle, "text");
    }

    public ProductDetailsPage clickOnOnesieProduct() {
        click(OnesieTitle);
        return new ProductDetailsPage();
    }

    public void scrollToOnesie() {
        scrollToElement(OnesieTitle);
    }

}