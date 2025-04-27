package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageobjects.HomePage;
import managers.DriverManager;
import utils.JsonReader;
import com.google.gson.JsonObject;

public class HomePageTests {
    
    @BeforeMethod
    public void setup() {
        DriverManager.getDriver();
    }
    
    @Test
    public void verifyHomePageTitle() {
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.getPageTitle().contains("BBC"));
    }
    
    @Test
    public void testNewsNavigation() {
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
        homePage.clickNewsLink();
        Assert.assertTrue(DriverManager.getDriver().getTitle().contains("News"));
    }
    
    @Test(dataProvider = "searchData")
    public void testSearchFunctionality(String searchTerm) {
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
        homePage.searchFor(searchTerm);
        Assert.assertTrue(homePage.isSearchTermPresentInHeadlines(searchTerm), 
                      "Search term not found in the headlines!");
    }
    
    @org.testng.annotations.DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        JsonObject testData = JsonReader.readJsonFile(
            "src/test/resources/testdata/bbc_testdata.json");
        return new Object[][] {
            {testData.get("searchTerm1").getAsString()},
            {testData.get("searchTerm2").getAsString()}
        };
    }
    
    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }
}