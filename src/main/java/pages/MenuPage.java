package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class MenuPage extends BasePage{
    private final By menuButton= AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView");

    public SettingPage pressMenuButton(){
        click(menuButton);
        return new SettingPage();
    }

}
