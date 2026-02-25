package com.ditto.utils;

import com.ditto.base.*;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

	@Override
	public void onStart(ITestContext context) {
		System.out.println("===== Test Execution Started =====");
	}

	@Override
	public void onTestStart(ITestResult result) {
		ExtentReportManager.createTest(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReportManager.getTest().pass("Test Passed Successfully");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentReportManager.getTest().fail(result.getThrowable());
		BasePage basePage = new BasePage();
		String screenshotPath = basePage.captureScreenshot(testName);
		ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReportManager.getTest().skip("Test Skipped: " + result.getThrowable());
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentReportManager.flushReport();
		ExtentReportManager.unload();
		System.out.println("===== Test Execution Completed =====");
	}
}
