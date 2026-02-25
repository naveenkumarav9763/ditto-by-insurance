package com.ditto.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ditto.base.BasePage;

public class ProductsPage extends BasePage {

	private By HEALTH_PRODUCT(String productName) {
		return By.xpath("//span[text()='" + productName + "']");
	}
	private static final By NEXT_BUTTON = By.xpath("//span[text()='Next']");
	private static final By ALL_ACCORDIANS = By.xpath("//div[contains(@class,'policyDetailAccordionItem')]");
	private static final By ALL_ACCORDIANS_NAME = By.xpath(
			"//div[contains(@class,'policyDetailAccordionItem')]//div[contains(@class,'policyDetailAccordionBadge')]/following-sibling::span");
	private static final By CONTINUE_BUTTON = By.xpath("//span[text()='Continue']");
	private static final By BENEFITS_HEADER = By.xpath("//h1[contains(text(),'Understand your policy')]");

	private void selectProduct() {
		String productName = testData.get("ProductName");
		addStepValidation(isElementPresent(HEALTH_PRODUCT(productName), getGlobalTimeOut()),
				productName + " Product is displayed in products page");
		captureScreenshot("Products page");
		clickOnElement(HEALTH_PRODUCT(productName));
	}

	private void validateBenefitsHeader() {
		addStepValidation(isElementPresent(BENEFITS_HEADER, getGlobalTimeOut()),
				"Navigated Successfully to benefits page after select the product");
	}

	private void expandAccordion(WebElement accordionLocator, String accordianName) {
		if (!accordianName.toUpperCase().contains("MAIN BENEFITS")) {
			scrollToElement(accordionLocator);
			clickOnElement(accordionLocator);
		}
	}

	private void clickOnNextButton(String accordianName) {
		if (accordianName.toUpperCase().contains("EXTRA BENEFITS")) {
			clickOnElement(CONTINUE_BUTTON);
		} else {
			clickOnElement(NEXT_BUTTON);
		}
	}

	private void validateAccordions() {
		List<WebElement> accordian = validateAndReturnList(findElements(ALL_ACCORDIANS), "All Accordian");
		List<WebElement> accordianNames = validateAndReturnList(findElements(ALL_ACCORDIANS_NAME),
				"All Accordian Name");
		for (int i = 0; i < accordian.size(); i++) {
			String accordianName = accordianNames.get(i).getText();
			expandAccordion(accordian.get(i), accordianName);
			String active = accordian.get(i).getAttribute("data-active");
			addStepValidation(active.contains("true"),
					accordianName + " Accordian Expanded Successfully after clicks on Next Button");
			captureScreenshot(accordianName + "Accordian");
			clickOnNextButton(accordianName);
		}
	}

	public void selectproductAndNavigateToTellUsAboutYouPage() {
		selectProduct();
		validateBenefitsHeader();
		validateAccordions();
	}
}
