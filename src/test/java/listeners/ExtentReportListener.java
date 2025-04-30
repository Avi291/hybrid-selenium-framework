package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import managers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListener implements ITestListener {
    private static final String REPORT_DIR = "test-output/extent-reports/";
    private ExtentReports extent;
    private ExtentTest test;

    @Override
    public void onStart(ITestContext context) {
        // Initialize report directory and ExtentSparkReporter
        Path reportPath = Paths.get(REPORT_DIR);
        if (!Files.exists(reportPath)) {
            try {
                Files.createDirectories(reportPath);
            } catch (IOException e) {
                System.err.println("Failed to create report directory: " + e.getMessage());
            }
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportName = "Test-Report_" + timeStamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_DIR + reportName);

        // Load config if exists
        try {
            File configFile = new File("src/test/resources/config/extent-config.xml");
            if (configFile.exists()) {
                spark.loadXMLConfig(configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Extent config file not found, using default settings");
        }

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework", "Hybrid Selenium");
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test Failed");
        test.log(Status.FAIL, result.getThrowable());
    
    // Get driver from DriverManager and capture screenshot
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                try {
                // Convert to relative path for ExtentReports
                    test.fail("Screenshot of failure", 
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } catch (Exception e) {  // Changed from IOException to Exception
                    test.warning("Failed to attach screenshot: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

}