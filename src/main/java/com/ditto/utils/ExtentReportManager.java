package com.ditto.utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public static ExtentReports getInstance() {
		if (extent == null) {
			File reportDir = new File("reports");
			if (!reportDir.exists()) {
				reportDir.mkdirs();
			}
			String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String reportPath = "reports/ExtentReport_" + timeStamp + ".html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
			sparkReporter.config().setReportName("Automation Execution Report");
			sparkReporter.config().setDocumentTitle("Hybrid Framework Report");
			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
			extent.setSystemInfo("Framework", "Hybrid Framework");
			extent.setSystemInfo("Tool", "Selenium 4");
			extent.setSystemInfo("Execution Mode", "Parallel");
			extent.setSystemInfo("Environment", "QA");
		}
		return extent;
	}

	public static void createTest(String testName) {
		ExtentTest extentTest = getInstance().createTest(testName);
		test.set(extentTest);
	}

	public static ExtentTest getTest() {
		return test.get();
	}

	public static void flushReport() {
		if (extent != null) {
			extent.flush();
		}
	}

	public static void unload() {
		test.remove();
	}
}
