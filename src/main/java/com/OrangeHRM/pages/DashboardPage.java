package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeWidgetElement;

public class DashboardPage extends BaseTestPage {

	WebDriver driver;

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}
	
	@FindBy(xpath="//*[normalize-space()='My Actions']/ancestor::div[@class='oxd-grid-item oxd-grid-item--gutters orangehrm-dashboard-widget']//div[@class='oxd-sheet oxd-sheet--rounded oxd-sheet--white orangehrm-dashboard-widget']")
	public OrangeWidgetElement widgetMyActions;
	
	@FindBy(xpath="//*[normalize-space()='Time at Work']/ancestor::div[@class='oxd-grid-item oxd-grid-item--gutters orangehrm-dashboard-widget']//div[@class='oxd-sheet oxd-sheet--rounded oxd-sheet--white orangehrm-dashboard-widget']")
	public OrangeWidgetElement widgetTimeAtWork;
	
	@FindBy(xpath="//*[normalize-space()='Quick Launch']/ancestor::div[@class='oxd-grid-item oxd-grid-item--gutters orangehrm-dashboard-widget']//div[@class='oxd-sheet oxd-sheet--rounded oxd-sheet--white orangehrm-dashboard-widget']")
	public OrangeWidgetElement widgetQuickLaunch;
	
	
	
	@Override
	public void waitForPageToLoad() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * This method does the following : Select all options in the view menu
	 * provide implementation to it.
	 */
	public void setDefaultState() {
		
	}
}
