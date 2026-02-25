package com.ditto.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	
	public DriverFactory() {
	}

	public static void initDriver(String browser) {
		switch (browser.toLowerCase()) {
		case "chrome":
			DriverFactory.driver.set(new ChromeDriver());
			break;
		case "firefox":
			DriverFactory.driver.set(new FirefoxDriver());
			break;
		case "edge":
			DriverFactory.driver.set(new EdgeDriver());
			break;
		default:
			throw new RuntimeException("Browser not supported: " + browser);
		}
		getDriver().manage().window().maximize();
	}

	public static WebDriver getDriver() {
		return (WebDriver) driver.get();
	}

	// Quit Driver
	public static void quitDriver() {

		if (driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}
}
