package pageobjects;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import managers.DriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import utils.ConfigReader;

public class HomePage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(css = "header[role='banner']")
    private WebElement header;
    
    @FindBy(xpath = "//a[contains(text(),'News')]")
    private WebElement newsLink;
    
    @FindBy(css = "input[data-testid='search-input-field']")
    private WebElement searchInput;
    
    @FindBy(css = "button[aria-label='Open menu']")
    private WebElement openMenu;

    @FindBy(css = "button[data-testid='search-input-search-button']")
    private WebElement searchButton;

    @FindBy(css = "h2[data-testid='card-headline']")
    private List<WebElement> headlineCards;

    @FindBy(css = "h2[data-testid='texas-title']")
    private WebElement moreNewsTitle;
    
    public HomePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public void navigateToHomePage() {
        driver.get(ConfigReader.getProperty("baseUrl"));
    }
    
    public boolean isHeaderDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(header));
        return header.isDisplayed();
    }
    
    public void clickNewsLink() {
        clickElement(newsLink);
    }
    
    public void searchFor(String term) {
        clickElement(openMenu);
        typeInField(searchInput, term);
        clickElement(searchButton);
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public boolean isSearchTermPresentInHeadlines(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfAllElements(headlineCards));
        for (WebElement headline : headlineCards) {
            String headlineText = headline.getText();
            if (headlineText.toLowerCase().contains(searchTerm.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public WebElement getMoreNewsTitle() {
        return moreNewsTitle;
    }
    
    // Helper Method to Click Element
    private void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    
    // Helper Method to Type in a Field
    private void typeInField(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }
}
