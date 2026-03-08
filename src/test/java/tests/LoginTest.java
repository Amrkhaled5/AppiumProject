package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.datamanager.JsonReader;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private JsonReader loginData;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage();
        loginData = new JsonReader("loginData.json");
    }

    @Test
    public void testLoginWithInValidCredentials() {
        loginPage.enterUserName(loginData.getJsonData("$.invalidUser.username"));
        loginPage.enterPassword(loginData.getJsonData("$.invalidUser.password"));
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }

    @Test
    public void testLoginWithValidCredentials() {
        loginPage.enterUserName(loginData.getJsonData("$.validUser.username"));
        loginPage.enterPassword(loginData.getJsonData("$.validUser.password"));
        productsPage = loginPage.clickLoginButton();

        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed");
    }
}