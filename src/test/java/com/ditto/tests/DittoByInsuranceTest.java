package com.ditto.tests;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.ditto.base.BasePage;
import com.ditto.base.PageFactory;
import com.ditto.utils.TestListener;

@Listeners(TestListener.class)
public class DittoByInsuranceTest extends BasePage{
	
	@Test
	public void PremiumCalculationTests() {
		try {
		PageFactory.getProductsPage().selectproductAndNavigateToTellUsAboutYouPage();
		PageFactory.getTellUsAboutYouPage().fillTellUsAboutYouForm();
		PageFactory.getPremiumCalculationPage().validatePremiumPriceWithAddon();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
