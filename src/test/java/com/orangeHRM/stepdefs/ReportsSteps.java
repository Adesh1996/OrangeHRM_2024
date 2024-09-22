package com.orangeHRM.stepdefs;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.pages.LeavePage;

import io.cucumber.java.en.And;

public class ReportsSteps extends BaseSteps {
	
	LeavePage leavePage;
	
	@And("^user selects report category \"([^\"]*)\"$")
	public void user_selects_report_category(String strArg1) {
		StringTokenizer tok = new StringTokenizer(strArg1, ".");
		List<String> tokenList = new ArrayList<>();
		
		while(tok.hasMoreTokens()) {
			tokenList.add(tok.nextToken());
		}
		
		for(String token: tokenList) {
			getWebElement("LeavePage.option" + token , LocalDriver.getDriver()).click();
		}
	}

}
