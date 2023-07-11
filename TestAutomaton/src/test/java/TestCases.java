import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.example.Common.Constants;

public class TestCases {

    ChromeDriver driver;

    @Test
    public void verifyLoginWithValidCredentials() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Constants.CHROME_ARGUMENT);
        driver = new ChromeDriver(chromeOptions);
        driver.get(Constants.WEB_URL);
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("pwd")).sendKeys("manager");

        driver.findElement(By.id("loginButton")).click();
    }

    @Test
    public void verifyLoginWithIncorrectUserName() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);

        driver.get(Constants.WEB_URL);
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("User");
        driver.findElement(By.name("pwd")).sendKeys("manager");

        driver.findElement(By.id("loginButton")).click();
    }

    @Test
    public void verifyLoginWithIncorrectPassword() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);

        driver.get(Constants.WEB_URL);
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("pwd")).sendKeys("Password");

        driver.findElement(By.id("loginButton")).click();
    }

    @Test
    public void reportsDashboard() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);

        driver.get(Constants.WEB_URL);
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("pwd")).sendKeys("manager");

        driver.findElement(By.id("loginButton")).click();

        // Wait for the login to complete and navigate to the desired URL
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/user/submit_tt.do"));

        driver.findElement(By.id("container_reports")).click();
    }

    @AfterMethod
    public void captureFailureScreenshot(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot and perform necessary actions for failure
            String testName = result.getName();
            String screenshotName = testName + ".png";
            String screenshotPath = System.getProperty("user.dir") + File.separator + "test" + File.separator + screenshotName;
            captureScreenshot(driver, screenshotPath);
            // Perform additional actions like logging, reporting, etc.
        }
        driver.quit();
    }

    // Method to capture screenshot
    public void captureScreenshot(WebDriver driver, String screenshotPath) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshotFile.toPath(), new File(screenshotPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}