package com.ditto.pages;

import org.openqa.selenium.By;

import com.ditto.base.BasePage;
import com.ditto.utils.ExtentReportManager;

public class PremiumCalculationPage extends BasePage{
	


	private static final By BASE_PREMIUM_AMOUNT = By.xpath("//span[text()='Base Premium']/following-sibling::span[text()]");
	private static final By TOTAL_PREMIUM_AMOUNT = By.xpath("//span[text()='Total Premium']/following-sibling::span[text()]");
	private By ADDONS_CHECKBOX(String addonName) {
		return By.xpath("//button[contains(@id,'control-addons')]//following-sibling::div//input[@name='"+addonName+"']");
	}
	private By ADDONS_PRICE(String addonName) {
		return By.xpath("//span[text()='"+addonName+"']/parent::div/parent::div/following-sibling::div/span[text()='Premium']/following-sibling::span");
	}
	
	private double getAmount(By locator) {
		String value = getText(locator).replaceAll("[^0-9.]", "").trim();		
		return Double.parseDouble(value);
	}
	
	private void selectAddon() {
		String addonName = testData.get("Addon");
		addStepValidation(isElementPresent(ADDONS_CHECKBOX(addonName), getGlobalTimeOut()), addonName+" Addon displayed in Premium calculation page");
		scrollToElement(ADDONS_CHECKBOX(addonName));
		clickOnElement(ADDONS_CHECKBOX(addonName));
	    boolean isAddonSelected = findElement(ADDONS_CHECKBOX(addonName)).isSelected();
		addStepValidation(isAddonSelected, addonName+" Addon is selected in Premium calculation page");
	}
	private void validatePremiumWithAddon() {
		String addonName = testData.get("Addon");
		ExtentReportManager.getTest().info("Validating premium calculation for addon: " + addonName);
		waitForVisibility(TOTAL_PREMIUM_AMOUNT);
		scrollToElement(BASE_PREMIUM_AMOUNT);
		double basePremium = getAmount(BASE_PREMIUM_AMOUNT);
		double addonPrice = getAmount(ADDONS_PRICE(addonName));
		double totalPremium = getAmount(TOTAL_PREMIUM_AMOUNT);
		double expectedTotal = basePremium + addonPrice;
		ExtentReportManager.getTest().info("Base Premium: " + basePremium);
		ExtentReportManager.getTest().info("Addon Price: " + addonPrice);
		ExtentReportManager.getTest().info("Expected Total: " + expectedTotal);
		ExtentReportManager.getTest().info("Actual Total: " + totalPremium);
		addStepValidation(Double.compare(totalPremium, expectedTotal) == 0,
				"Total premium should be Base Premium + Addon Price");
	}
	
	public void validatePremiumPriceWithAddon() {
		selectAddon();
		validatePremiumWithAddon();
	}
}
