package com.OrangeHRM.utilities;

import java.util.Map;

import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;

public class ScenerioDataProvider {
	
	private ScenerioDataProvider() {
		throw new IllegalStateException("Utility Class");
	}
	
	public static Map<String, String> getData(String scenerioName) {
		ExcelUtilities excelUtils = null;
		try {
			switch(AppConstants.APPLICATION_ENV.toLowerCase()) {
			case "orangehrm":
				excelUtils = new ExcelUtilities(AppConstants.USER_DIRECTORY + "/test-data/OrangeHRM.xlsx");
				break;
			case "orangehrmuat":
				excelUtils = new ExcelUtilities(AppConstants.USER_DIRECTORY + "/test-data/OrangeHRMUAT.xlsx");
				break;
			default :
				Assert.fail("*****Environment is not properly set*****");
			}
			return OrangeUtils.getData(scenerioName, excelUtils);
		}   
		catch(Exception e) {
			Assert.fail(e.getLocalizedMessage());
			return null;
		}
	}

}
