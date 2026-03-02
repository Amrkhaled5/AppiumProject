package Authentication;

import Pages.LoginPage;
import Pages.ProductsPage;
import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class LoginTest{
    LoginPage loginPage;
    ProductsPage productsPage;
    BaseClass baseClass;

    @Parameters({"platformName","deviceName"})
    @BeforeClass
    public void beforeClass(String platformName, String deviceName) throws IOException {
        baseClass = new BaseClass();
        baseClass.setUpDriver(platformName, deviceName);
        loginPage = new LoginPage(baseClass.getDriver());
    }

    @AfterClass
    public void afterClass() {
        baseClass.quitDriver();
    }

    @Test
    public void testLoginWithInValidCredentials() {
        loginPage.enterUserName("Amr Khaled");
        loginPage.enterPassword("123456");
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.isErrorMessageDisplayedCorrectError("Username and password do not match any user in this service."), "Error message should indicate invalid credentials");
    }

    @Test
    public void testLoginWithValidCredentials() {
        loginPage.enterUserName("standard_user");
        loginPage.enterPassword("secret_sauce");
        productsPage=loginPage.clickLoginButton();
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after successful login");
    }

}
