package pages;

import io.appium.java_client.AppiumBy;
import net.bytebuddy.implementation.bytecode.ShiftRight;
import org.openqa.selenium.By;

public class ProductsPage extends BasePage {
    private final By productTitle = AppiumBy.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]");
    private final By BackpackProductTitle = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]");
    private final By BackpackProductPrice = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"test-Price\" and @text=\"$29.99\"]");

    public boolean isProductsPageDisplayed() {
        return isElementDisplayed(productTitle);
    }

    public String getProductTitle() {
        return getAttribute(BackpackProductTitle, "text");
    }

    public String getProductPrice() {
        return getAttribute(BackpackProductPrice, "text");
    }

    public ProductDetailsPage clickOnBackpackProduct() {
        click(BackpackProductTitle);
        return new ProductDetailsPage();
    }

}