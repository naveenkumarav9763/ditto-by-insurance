package com.ditto.pages;

import org.openqa.selenium.By;
import com.ditto.base.BasePage;

public class TellUsAboutYouPage extends BasePage {

	private By SELF_GENDER(String gender) {
		return By.xpath("//span[text()='Self']/following-sibling::div//div[text()='" + gender + "']");
	}

	private static final By GENDER_SELECT_LOCATOR = By.xpath(
			"//span[contains(text(),'Self')]/parent::div/parent::span/parent::label[contains(@class,'memberChip')]");
	private static final By NEXT_STEP_BUTTON = By.xpath("//span[text()='Next step']");
	private static final By YOUR_AGE = By.xpath("//input[@placeholder='Your age']");
	private static final By PINCODE = By.xpath("//input[@name='pincode']");
	private static final By CALCULATE_AMOUNT_BUTTON = By.xpath("//span[text()='Calculate Premium']");

	/**
	 * Selects gender option and validates selection.
	 */
	private void selectGender() {
		String gender = testData.get("Gender");
		addStepValidation(isElementPresent(SELF_GENDER(gender), getGlobalTimeOut()),
				gender + " selection option is displayed in Tell us About You page");
		clickOnElement(SELF_GENDER(gender));
		String isGenderSelected = getAttribute(GENDER_SELECT_LOCATOR, "data-checked");
		addStepValidation(isGenderSelected != null && isGenderSelected.contains("true"),
				"Gender : '" + gender + "' is selected successfully");
		captureScreenshot("Gender");
	}

	/**
	 * Clicks on Next Step button after validating it is enabled.
	 */
	private void clickNextStep() {
		addStepValidation(isElementEnabled(NEXT_STEP_BUTTON, getGlobalTimeOut()),
				"Next Button is Enabled after select the gender");
		clickOnElement(NEXT_STEP_BUTTON);
	}

	/**
	 * Enters age and validates the entered value.
	 */
	private void enterAge() {
		String age = testData.get("Age");
		waitForVisibility(YOUR_AGE);
		clearAndSendKeys(YOUR_AGE, age);
		String enteredAge = getAttribute(YOUR_AGE, "value");
		addStepValidation(enteredAge.equals(age),
				"Entered age is matched with provided age");
	}

	/**
	 * Enters pincode and validates the entered value.
	 */
	private void enterPinCode() {
		String pincode = testData.get("Pincode");
		waitForVisibility(PINCODE);
		clearAndSendKeys(PINCODE, pincode);
		String enteredPincode = getAttribute(PINCODE, "value");
		addStepValidation(enteredPincode.equals(pincode),
				"Entered Pincode is matched with provided pincode");
		captureScreenshot("Tell Us About Form");
	}

	/**
	 * Clicks on Calculate Premium button after validation.
	 */
	private void clickCalculateAmount() {
		addStepValidation(isElementEnabled(CALCULATE_AMOUNT_BUTTON, getGlobalTimeOut()),
				"Calculate Premium button is enabled");
		clickOnElement(CALCULATE_AMOUNT_BUTTON);
	}

	/**
	 * Fills Tell Us About You form and submits it.
	 */
	public void fillTellUsAboutYouForm() {
		selectGender();
		clickNextStep();
		enterAge();
		enterPinCode();
		clickCalculateAmount();
	}
}