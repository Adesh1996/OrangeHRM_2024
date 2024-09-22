package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeTableElement;

public class RecruitmentPage extends BaseTestPage {

	WebDriver driver;

	public RecruitmentPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}
	
	@FindBy(xpath="//div[@role='table']")
	public OrangeTableElement tableRecruitment;

	@Override
	public void waitForPageToLoad() {
		startTime = System.currentTimeMillis();
		if (super.isPageLoaded(driver)) {
			try {
				endTime = System.currentTimeMillis();
				pageLoadTime = (endTime - startTime) / 1000;
			} catch (Exception e) {
				Assert.fail("Expected page did not load\n");
			}
		} else {
			Assert.fail("Timed out after " + AppConstants.PAGE_LOAD_TIMEOUT + " seconds\n");
		}

	}

}
