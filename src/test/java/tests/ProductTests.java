package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.datamanager.JsonReader;

public class ProductTests extends BaseTest {
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private MenuPage menuPage;
    private SettingPage settingPage;
    private JsonReader loginData;
    private ProductDetailsPage productDetailsPage;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage();
        loginData = new JsonReader("loginData.json");
    }

    @Test
    public void validateProductOnProductsPage() {
        loginPage.enterUserName(loginData.getJsonData("$.validUser.username"));
        loginPage.enterPassword(loginData.getJsonData("$.validUser.password"));
        productsPage = loginPage.clickLoginButton();
        String productTitle = productsPage.getProductTitle();
        productDetailsPage=productsPage.clickOnBackpackProduct();

        Assert.assertEquals(productTitle, productDetailsPage.getProductTitle(), "Product title should match on product details page");

    }
}
