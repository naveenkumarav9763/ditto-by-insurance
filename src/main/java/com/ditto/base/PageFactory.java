package com.ditto.base;

public class PageFactory {
	
	public static com.ditto.pages.ProductsPage getProductsPage(){
		return new com.ditto.pages.ProductsPage();
	}
	
	public static com.ditto.pages.TellUsAboutYouPage getTellUsAboutYouPage(){
		return new com.ditto.pages.TellUsAboutYouPage();
	}
	public static com.ditto.pages.PremiumCalculationPage getPremiumCalculationPage(){
		return new com.ditto.pages.PremiumCalculationPage();
	}
}
