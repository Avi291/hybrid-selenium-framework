package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import managers.DriverManager;
import java.time.Duration;

public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    // Scroll to bottom of page
    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    // Scroll to top of page
    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0)");
    }

    // Scroll to specific element
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Smooth scroll to element
    public void smoothScrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    // Scroll by pixel offset
    public void scrollByPixels(int x, int y) {
        js.executeScript("window.scrollBy(" + x + "," + y + ")");
    }
}