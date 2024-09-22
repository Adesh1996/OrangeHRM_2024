package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeWebElement;

public class LeavePage extends BaseTestPage {

	WebDriver driver;

	public LeavePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}

	@FindBy(xpath = "//a[normalize-space()='My Leave']")
	public OrangeWebElement optionmyLeave;
	
	@FindBy(xpath = "//a[normalize-space()='Apply']")
	public OrangeWebElement optionApply;
	
	@FindBy(xpath = "//span[normalize-space()='Entitlements']")
	public OrangeWebElement optionEntitlements;
	
	@FindBy(xpath = "//span[normalize-space()='Reports']")
	public OrangeWebElement optionReports;
	
	@FindBy(xpath = "//span[normalize-space()='Configure']")
	public OrangeWebElement optionConfigure;
	
	@FindBy(xpath = "//a[normalize-space()='Leave List']")
	public OrangeWebElement optionLeaveList;
	
	@FindBy(xpath = "//a[normalize-space()='Assign Leave']")
	public OrangeWebElement optionAssignLeave;

	@FindBy(xpath="//a[normalize-space()='Leave Entitlements and Usage Report']")
	public OrangeWebElement optionLeaveEntitlementsandUsageReport;
	
	@FindBy(xpath="//a[normalize-space()='My Leave Entitlements and Usage Report']")
	public OrangeWebElement optionMyLeaveEntitlementsAndUsageReport;
	
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
