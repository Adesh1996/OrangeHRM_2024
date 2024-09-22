package com.orangeHRM.stepdefs;

import java.util.Base64;
import java.util.Scanner;

import org.testng.SkipException;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.constants.AppConstants;
import com.OrangeHRM.pages.CommonMenuPage;
import com.OrangeHRM.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps extends BaseSteps {

	LoginPage loginPage = null;
	CommonMenuPage commonMenuPage = null;

	@Given("^user is on OrangeHRM Login Page$")
	public void user_is_on_OrangeHRM_login_page() {
		if (scenarioData.get().size() == 0) {
			throw new SkipException("***** Test Case : " + scenario.get().getName()
					+ " not found in Test Data sheet. Please add an entry of this test case. *****\n");
		}

		LocalDriver.getDriver().get(AppConstants.getApplicationURL());
		loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.waitForPageToLoad();
	}

	@Then("user logs OrangeHRM with \"([^\"]*)\"$")
	public void user_logs_orange_hrm_with(String userName) {
		loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.waitForPageToLoad();
		userName = getVariableValue(userName);
		userName = userName.toLowerCase();
		switch (userName) {
		case "admin":
			loginPage.loginWithAdmin();
			break;
		case "admin1":
			loginPage.loginWithAdmin1();
			break;
			
		default:
			throw new AssertionError("Given user is not found");

		}
		loginPage.waitForPageToLoad();
	}
	
	@When("user logs out of OrangeHRM")
	public void user_logs_out_of_orange_hrm() {
		commonMenuPage = new CommonMenuPage(LocalDriver.getDriver());
		commonMenuPage.doLogout();
	}
	
	
	@When("^user enters user id \"([^\"]*)\"$")
	public void user_enters_user_id(String userID) {
		userID = getVariableValue(userID);
		LoginPage loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.textboxUsername.sendKeys(userID);
	}
	
	@When("^user enters password \"([^\"]*)\"$")
	public void user_enters_password(String encryptedPassword) {
		encryptedPassword = getVariableValue(encryptedPassword);
		LoginPage loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.textboxPassword.sendKeys(new String(Base64.getDecoder().decode(encryptedPassword)));
	}
	
	@Then("^user enters credentials from command line$")
	public void user_enters_credentials_from_command_line() {
		System.out.println("Please enter User ID : ");
		Scanner scanner = new Scanner(System.in);
		LoginPage loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.textboxUsername.sendKeys(scanner.next());
		
		Scanner scanner1 = new Scanner(System.in);
		loginPage.textboxPassword.sendKeys(scanner1.next());
		scanner.close();
		scanner1.close();
		
		
		loginPage.buttonLogin.click();
		
	}
	
	@When("^user logs into application with custom credentials$")
	public void user_logs_into_application_with_custom_credentials() {
		LoginPage loginPage = new LoginPage(LocalDriver.getDriver());
		loginPage.textboxUsername.sendKeys(AppConstants.APPLICATION_CLIENT);
		loginPage.textboxPassword.sendKeys(AppConstants.APPLICATION_CLIENT); // Update Password here
	}
	
}
