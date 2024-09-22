package com.OrangeHRM.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.customelements.CustomElementFieldDecorator;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.utilities.OrangeUtils;

public class LoginPage extends BaseTestPage {

	WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new CustomElementFieldDecorator(driver, driver), this);
	}

	@FindBy(xpath = "//input[@name='username']")
	public OrangeWebElement textboxUsername;

	@FindBy(xpath = "//input[@name='password']")
	public OrangeWebElement textboxPassword;

	@FindBy(xpath = "//button[@type='submit']")
	public OrangeWebElement buttonLogin;

	@FindBy(xpath = "//p[(text())='Forgot your password? ']")
	public OrangeWebElement linkForgotyourPassword;

	public void loginWithAdmin() {
		OrangeUtils.wait(2000);
		textboxUsername.sendKeys("Admin");
		textboxPassword.sendKeys("admin123");
		buttonLogin.click();
	}

	public void loginWithAdmin1() {
		OrangeUtils.wait(2000);
		textboxUsername.sendKeys("admin1");
		textboxPassword.sendKeys("Admin123");
		buttonLogin.click();
	}
	
	
	
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
