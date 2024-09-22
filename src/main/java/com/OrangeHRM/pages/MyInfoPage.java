package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeWebElement;

public class MyInfoPage extends BaseTestPage {

	WebDriver driver;

	public MyInfoPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}
	

	@FindBy(xpath="//a[normalize-space()='Personal Details']")  
	public OrangeWebElement linkpersonalDetails;
	
	@FindBy(xpath="//a[normalize-space()='Contact Details']")  
	public OrangeWebElement linkcontactDetails;

	
	@FindBy(xpath="//a[normalize-space()='Dependents']") 
	public OrangeWebElement linkdependents;
	
	@FindBy(xpath="//a[normalize-space()='Immigration']") 
	public OrangeWebElement linkimmigration;
	
	@FindBy(xpath="//a[normalize-space()='Job']")  
	public OrangeWebElement linkjob;
	
	@FindBy(xpath="//a[normalize-space()='Salary']") 
	public OrangeWebElement linksalary;
	
	@FindBy(xpath="//a[normalize-space()='Tax Exemptions']") 
	public OrangeWebElement linktaxExemptions;
	
	@FindBy(xpath="//a[normalize-space()='Report-to']") 
	public OrangeWebElement linkreportTo;
	
	@FindBy(xpath="//a[normalize-space()='Qualifications']")  
	public OrangeWebElement linkqualifications;
	
	@FindBy(xpath="//a[normalize-space()='Memberships']") 
	public OrangeWebElement linkmemberships;
	
	//File Upload Locators:   
	@FindBy(xpath="//button[normalize-space()='Add']")
	public OrangeWebElement  buttonAdd;
	
	@FindBy(xpath="//div[@class='oxd-file-button']")
	public OrangeWebElement  buttonBrowse;

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
