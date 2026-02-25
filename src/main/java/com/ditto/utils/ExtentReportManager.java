package com.ditto.utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ditto.base.BasePage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static String reportPath;

	public static ExtentReports getInstance() {
		if (extent == null) {
			File reportDir = new File("test-output/reports");
			if (!reportDir.exists()) {
				reportDir.mkdirs();
			}
			String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			reportPath = System.getProperty("user.dir")+"test-output/reports/ExtentReport_" + timeStamp + ".html";
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
			sparkReporter.config().setReportName("Ditto Insurance Automation Execution Report");
			sparkReporter.config().setDocumentTitle("Ditto Insurance Automation Execution Report");
			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
			extent.setSystemInfo("Framework", "Hybrid Framework");
			extent.setSystemInfo("Tool", "Selenium");
			boolean isCI =  new BasePage().isCIEnvironment();
			if(isCI){
				extent.setSystemInfo("Execution Mode", "CI");
			}else{
				extent.setSystemInfo("Execution Mode", "Local");
			}
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
			System.out.println("========================================");
            System.out.println("Extent Report Generated:");
            System.out.println(reportPath);
            System.out.println("========================================");
		}
	}

	public static void unload() {
		test.remove();
	}
}
