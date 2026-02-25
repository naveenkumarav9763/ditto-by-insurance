package com.ditto.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public DriverFactory() {
	}

	public static void initDriver(String browser) {
		boolean isCI =  new BasePage().isCIEnvironment();
		switch (browser.toLowerCase()) {
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				if (isCI) {
					chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
					chromeOptions.addArguments("--window-size=1920,4000");
					chromeOptions.addArguments("--start-maximized");
				}
				driver.set(new ChromeDriver(chromeOptions));
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (isCI) {
					edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
					edgeOptions.addArguments("--window-size=1920,4000");
					edgeOptions.addArguments("--start-maximized");
				}
				driver.set(new EdgeDriver(edgeOptions));
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
