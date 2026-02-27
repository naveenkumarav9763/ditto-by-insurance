package com.ditto.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.ditto.utils.ExcelUtil;
import com.ditto.utils.ExtentReportManager;
import com.ditto.utils.PropertyReader;


public class BasePage {

	protected WebDriver driver;
	protected static Hashtable<String, String> testData;
	private PropertyReader property;

	/**
	 * Constructor initializes WebDriver and PropertyReader.
	 */
	public BasePage() {
		this.driver = DriverFactory.getDriver();
		this.property = new PropertyReader();
	}

	/**
	 * Initializes the WebDriver before each test method.
	 * Launches browser, navigates to URL, and sets implicit wait.
	 */
	@BeforeMethod
	public void setUp() {
		DriverFactory.initDriver(property.getBrowser());
		driver = DriverFactory.getDriver();
		driver.get(property.getUrl());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getGlobalTimeOut()));
	}

	/**
	 * Quits the WebDriver instance after each test method.
	 */
	@AfterMethod
	public void tearDown() {
		DriverFactory.quitDriver();
	}

	/**
	 * Loads test data from Excel file before executing test class.
	 */
	@BeforeClass
	public void loadTestData() {
		testData = ExcelUtil.getTestDataAsHashtable("src/main/resources/excelData/dittoByInsuranceTestData.xlsx",
				"dittoByInsuranceTestData");
	}

	/**
	 * Captures screenshot and stores it under reports directory.
	 *
	 * @param testName Name of the test for screenshot file naming.
	 * @return Relative path of saved screenshot.
	 */
	public String captureScreenshot(String testName) {
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String directory = "test-output/reports/screenshots/";
		File reportDir = new File(directory);
		if (!reportDir.exists()) {
			reportDir.mkdirs();
		}
		String fileName = testName + "_" + System.currentTimeMillis() + ".png";
		String filePath = directory + fileName;
		try {
			Files.copy(srcFile.toPath(), Paths.get(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "screenshots/" + fileName;
	}

	/**
	 * Validates a condition and logs result with screenshot in Extent Report.
	 *
	 * @param condition Validation condition.
	 * @param message   Validation message.
	 */
	public void addStepValidation(boolean condition, String message) {
		ExtentReportManager.getTest().info("Validating: " + message);
		String filePath = captureScreenshot(message.replaceAll("[^a-zA-Z0-9]", ""));
		try {
			Assert.assertTrue(condition, message);
			ExtentReportManager.getTest().pass("Validation Passed: " + message,
					MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
		} catch (AssertionError e) {
			ExtentReportManager.getTest().fail("Validation Failed: " + message,
					MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
			throw e;
		}
	}

	/**
	 * Clicks on a web element using locator.
	 *
	 * @param locator By locator.
	 */
	public void clickOnElement(By locator) {
		driver.findElement(locator).click();
		ExtentReportManager.getTest().info("Clicked on: " + locator.toString());
	}

	/**
	 * Clicks on a WebElement.
	 *
	 * @param locator WebElement instance.
	 */
	public void clickOnElement(WebElement locator) {
		locator.click();
		ExtentReportManager.getTest().info("Clicked on: " + locator.toString());
	}

	/**
	 * Clears existing value and sends new input to element.
	 *
	 * @param locator Element locator.
	 * @param value   Input value.
	 */
	public void clearAndSendKeys(By locator, String value) {
		WebElement element = driver.findElement(locator);
		element.clear();
		element.sendKeys(value);
		ExtentReportManager.getTest().info("Entered value: " + value);
	}

	/**
	 * Retrieves visible text from element.
	 *
	 * @param locator Element locator.
	 * @return Text value.
	 */
	public String getText(By locator) {
		String text = driver.findElement(locator).getText();
		ExtentReportManager.getTest().info("Fetched text: " + text);
		return text;
	}

	/**
	 * Returns global timeout value from properties file.
	 *
	 * @return Timeout in seconds.
	 */
	public int getGlobalTimeOut() {
		return property.getTimeOut();
	}

	/**
	 * Returns WebDriverWait instance.
	 *
	 * @return WebDriverWait object.
	 */
	public WebDriverWait getWait() {
		return new WebDriverWait(driver, Duration.ofSeconds(property.getTimeOut()));
	}

	/**
	 * Waits until element becomes visible.
	 *
	 * @param locator Element locator.
	 * @return WebElement after visibility.
	 */
	public WebElement waitForVisibility(By locator) {
		return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Retrieves attribute value of element.
	 *
	 * @param locator       Element locator.
	 * @param attributeName Attribute name.
	 * @return Attribute value.
	 */
	public String getAttribute(By locator, String attributeName) {
		WebElement element = driver.findElement(locator);
		String attributeValue = element.getAttribute(attributeName);
		ExtentReportManager.getTest().info("Fetched attribute '" + attributeName + "' with value: " + attributeValue);
		return attributeValue;
	}

	/**
	 * Checks if element is present and displayed within timeout.
	 *
	 * @param locator          Element locator.
	 * @param timeoutInSeconds Timeout duration.
	 * @return true if displayed, else false.
	 */
	public boolean isElementPresent(By locator, int timeoutInSeconds) {
		try {
			boolean status = waitForVisibility(locator).isDisplayed();
			return status;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if element is enabled and clickable.
	 *
	 * @param locator          Element locator.
	 * @param timeoutInSeconds Timeout duration.
	 * @return true if enabled, else false.
	 */
	public boolean isElementEnabled(By locator, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			return element.isEnabled();
		} catch (ElementNotInteractableException e) {
			return false;
		}
	}

	/**
	 * Validates list is not empty.
	 *
	 * @param elements List of elements.
	 * @param listName Name for reporting.
	 * @return Validated list.
	 */
	public List<WebElement> validateAndReturnList(List<WebElement> elements, String listName) {
		if (elements == null || elements.isEmpty()) {
			throw new NoSuchElementException("List '" + listName + "' contains no elements");
		}
		return elements;
	}

	/**
	 * Finds single element using locator.
	 *
	 * @param locator Element locator.
	 * @return WebElement.
	 */
	public WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	/**
	 * Finds multiple elements using locator.
	 *
	 * @param locator Element locator.
	 * @return List of WebElements.
	 */
	public List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}

	/**
	 * Scrolls to element using locator.
	 *
	 * @param locator Element locator.
	 */
	public void scrollToElement(By locator) {
		scrollToElement(findElement(locator));
	}

	/**
	 * Scrolls to given WebElement using JavaScript.
	 *
	 * @param locator WebElement instance.
	 */
	public void scrollToElement(WebElement locator) {
		int x = locator.getLocation().getX();
		int y = locator.getLocation().getY();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(" + x + "," + (y - 200) + ")");
	}

	/**
	 * Determines whether execution is running in CI environment.
	 *
	 * @return true if CI, otherwise false.
	 */
	public boolean isCIEnvironment() {
		String envProperty = System.getProperty("env");
		String ciEnvVariable = System.getenv("CI");
		return "ci".equalsIgnoreCase(envProperty)
				|| "true".equalsIgnoreCase(ciEnvVariable);
	}
}