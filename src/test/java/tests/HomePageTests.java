package tests;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageobjects.HomePage;
import managers.DriverManager;
import utils.JsonReader;
import utils.ScreenshotUtils;
import com.google.gson.JsonObject;

public class HomePageTests extends BaseTest {
    
    @Test
    public void verifyHomePageTitle() {
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
        Assert.assertTrue(homePage.getPageTitle().contains("BBC"));
        // Capture screenshot with status in filename
        ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), 
            "verifyHomePageTitle");
    }
    
    @Test
    public void testNewsNavigation() throws InterruptedException {
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();
        homePage.clickNewsLink();
        // Capture screenshot with status in filename
        ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), 
            "testNewsNavigation");
        Assert.assertTrue(DriverManager.getDriver().getTitle().contains("News"));
        scrollToBottom();
        System.out.println("Scrolled to bottom of the page");
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
    
}