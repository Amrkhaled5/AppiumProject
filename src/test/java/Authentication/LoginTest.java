package Authentication;

import Pages.LoginPage;
import Pages.ProductsPage;
import base.BaseClass;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;

public class LoginTest{
    LoginPage loginPage;
    ProductsPage productsPage;
    BaseClass baseClass;
    InputStream dataFileInputStream;
    JSONObject loginTestData;
    String dataFileName="TestData/loginData.json";

    @Parameters({"platformName","deviceName"})
    @BeforeClass
    public void beforeClass(String platformName, String deviceName) throws IOException {
        try{
            dataFileInputStream = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(dataFileInputStream);
            loginTestData = new JSONObject(tokener);
        }catch (Exception e){
            System.err.println("Failed to read test data file: " + dataFileName + " Error: " + e.getMessage());
        } finally {
            if (dataFileInputStream != null) {
                dataFileInputStream.close();
            }
        }

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
        productsPage=loginPage.clickLoginButton();
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after successful login");
    }

}
