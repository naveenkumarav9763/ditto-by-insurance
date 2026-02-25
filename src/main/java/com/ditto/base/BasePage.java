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

	public BasePage() {
		this.driver = DriverFactory.getDriver();
		this.property = new PropertyReader();

	}

	@BeforeMethod
	public void setUp() {
		DriverFactory.initDriver(property.getBrowser());
		driver = DriverFactory.getDriver();
		driver.get(property.getUrl());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getGlobalTimeOut()));
	}

	@AfterMethod
	public void tearDown() {
		DriverFactory.quitDriver();
	}

	@BeforeClass
	public void loadTestData() {
		testData = ExcelUtil.getTestDataAsHashtable("src/main/resources/excelData/dittoByInsuranceTestData.xlsx",
				"dittoByInsuranceTestData");
	}

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
		return filePath;
	}

	public void addStepValidation(boolean condition, String message) {
		ExtentReportManager.getTest().info("Validating: " + message);
		String filePath = captureScreenshot(message.replaceAll("[^a-zA-Z0-9]", ""));
		try {
			Assert.assertTrue(condition, message);
			ExtentReportManager.getTest().pass("Validation Passed: " + message, MediaEntityBuilder
					.createScreenCaptureFromPath(filePath)
					.build());
		} catch (AssertionError e) {
			ExtentReportManager.getTest().fail("Validation Failed: " + message, MediaEntityBuilder
					.createScreenCaptureFromPath(filePath)
					.build());
			throw e;
		}
	}

	public void clickOnElement(By locator) {
		driver.findElement(locator).click();
		ExtentReportManager.getTest().info("Clicked on: " + locator.toString());
	}

	public void clickOnElement(WebElement locator) {
		locator.click();
		ExtentReportManager.getTest().info("Clicked on: " + locator.toString());
	}

	public void clearAndSendKeys(By locator, String value) {
		WebElement element = driver.findElement(locator);
		element.clear();
		element.sendKeys(value);
		ExtentReportManager.getTest().info("Entered value: " + value);
	}

	public String getText(By locator) {
		String text = driver.findElement(locator).getText();
		ExtentReportManager.getTest().info("Fetched text: " + text);
		return text;
	}

	public int getGlobalTimeOut() {
		return property.getTimeOut();
	}

	public WebDriverWait getWait() {
		return new WebDriverWait(driver, Duration.ofSeconds(property.getTimeOut()));
	}

	public WebElement waitForVisibility(By locator) {
		return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public String getAttribute(By locator, String attributeName) {
		WebElement element = driver.findElement(locator);
		String attributeValue = element.getAttribute(attributeName);
		ExtentReportManager.getTest().info("Fetched attribute '" + attributeName + "' with value: " + attributeValue);
		return attributeValue;
	}

	public boolean isElementPresent(By locator, int timeoutInSeconds) {
		try {
			boolean status = waitForVisibility(locator).isDisplayed();
			ExtentReportManager.getTest()
					.info("Element displayed: " + locator + " within " + timeoutInSeconds + " seconds");
			return status;
		} catch (NoSuchElementException e) {
			String errorMessage = "Element NOT displayed: " + locator + " after waiting " + timeoutInSeconds
					+ " seconds";
			ExtentReportManager.getTest().info(errorMessage);
			return false;
		}
	}

	public boolean isElementEnabled(By locator, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			boolean status = element.isEnabled();
			ExtentReportManager.getTest().info("Element enabled status for " + locator + " : " + status);
			return status;
		} catch (ElementNotInteractableException e) {
			String errorMessage = "Element not enabled within timeout: " + locator;
			ExtentReportManager.getTest().fail(errorMessage);
			return false;
		}
	}

	public List<WebElement> validateAndReturnList(List<WebElement> elements, String listName) {
		ExtentReportManager.getTest().info("Validating list: " + listName);
		if (elements == null || elements.isEmpty()) {
			ExtentReportManager.getTest().fail("List '" + listName + "' is empty");
			throw new NoSuchElementException("List '" + listName + "' contains no elements");
		}
		ExtentReportManager.getTest().pass("List '" + listName + "' contains " + elements.size() + " elements");
		return elements;
	}

	public WebElement findElement(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			ExtentReportManager.getTest().info("Element found: " + locator);
			return element;
		} catch (NoSuchElementException e) {
			String errorMessage = "Element not found using locator: " + locator;
			ExtentReportManager.getTest().fail(errorMessage);
			throw new NoSuchElementException(errorMessage);
		}
	}

	public List<WebElement> findElements(By locator) {
		List<WebElement> elements = driver.findElements(locator);
		return elements;
	}

	public void scrollToElement(By locator) {
		scrollToElement(findElement(locator));
	}

	public void scrollToElement(WebElement locator) {
		int x = locator.getLocation().getX();
		int y = locator.getLocation().getY();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(" + x + "," + (y - 200) + ")", new Object[] { "" });
	}

	public boolean isCIEnvironment() {
		String envProperty = System.getProperty("env");
		String ciEnvVariable = System.getenv("CI");
		return "ci".equalsIgnoreCase(envProperty)
				|| "true".equalsIgnoreCase(ciEnvVariable);
	}

}
