package pages;

import org.openqa.selenium.By;

public class ProductsPage extends BasePage {
    private final By productTitle = By.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]");

    public boolean isProductsPageDisplayed() {
        return isElementDisplayed(productTitle);
    }
}