package tests;

import base.BaseTest; // <-- Inheriting the core engine
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

import java.io.InputStream;

public class LoginTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginTestData;

    @BeforeMethod
    public void setupTest() {
        loginPage = new LoginPage();

        try (InputStream dataFileInputStream = getClass().getClassLoader().getResourceAsStream("testdata/loginData.json")) {
            if (dataFileInputStream != null) {
                JSONTokener tokener = new JSONTokener(dataFileInputStream);
                loginTestData = new JSONObject(tokener);
            }
        } catch (Exception e) {
            System.err.println("Failed to read test data file: " + e.getMessage());
        }
    }

    @Test
    public void testLoginWithInValidCredentials() {
        loginPage.enterUserName(loginTestData.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginTestData.getJSONObject("invalidUser").getString("password"));
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.isErrorMessageDisplayedCorrectError("Username and password do not match any user in this service."), "Error message should indicate invalid credentials");
    }

    @Test
    public void testLoginWithValidCredentials() {
        loginPage.enterUserName(loginTestData.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginTestData.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.clickLoginButton();

        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after successful login");
    }
}