package com.ditto.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {
	private Properties prop;

	public PropertyReader() {
		try {
			FileInputStream fis = new FileInputStream("src/main/resources/propertyFile/constants.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load constant file");
		}
	}

	public String getBrowser() {
		return prop.getProperty("browser");
	}

	public String getUrl() {
		return prop.getProperty("url");
	}

	public int getTimeOut() {
		return Integer.parseInt(prop.getProperty("TimeOut"));
	}
}
