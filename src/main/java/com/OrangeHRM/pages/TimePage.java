package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeTableElement;

public class TimePage extends BaseTestPage{
	
	WebDriver driver;
	
	public TimePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}
	
	
	@FindBy(xpath="//div[@role='table']")  
	public OrangeTableElement tableTimesheetsPendingAction;

	@Override
	public void waitForPageToLoad() {
		// TODO Auto-generated method stub
		
	}
	

}
