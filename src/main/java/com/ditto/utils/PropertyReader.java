package com.ditto.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {

	private Properties prop;

	/**
	 * Loads properties from constants.properties file.
	 */
	public PropertyReader() {
		try {
			FileInputStream fis = new FileInputStream("src/main/resources/propertyFile/constants.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load constant file");
		}
	}

	/**
	 * Returns configured browser name.
	 * @return browser name
	 */
	public String getBrowser() {
		return prop.getProperty("browser");
	}

	/**
	 * Returns application URL.
	 * @return url
	 */
	public String getUrl() {
		return prop.getProperty("url");
	}

	/**
	 * Returns global timeout value.
	 * @return timeout in seconds
	 */
	public int getTimeOut() {
		return Integer.parseInt(prop.getProperty("TimeOut"));
	}
}