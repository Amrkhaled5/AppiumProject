package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class ProductDetailsPage extends BasePage {
    private final By productTitle= AppiumBy.xpath("//android.widget.TextView[@text=\"Sauce Labs Onesie\"]");
    private final By productDescription= AppiumBy.accessibilityId("//android.widget.TextView[@text=\"carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.\"]");

    public String getProductTitle(){
        return getAttribute(productTitle,"text");
    }

    public String getProductDescription(){
        return getAttribute(productDescription,"text");
    }


}
