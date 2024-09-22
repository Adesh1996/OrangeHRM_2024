package com.OrangeHRM.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to provide uiMap to service attributes
 */
public class UItoServiceFieldMapping {
	
	private UItoServiceFieldMapping() {
		throw new IllegalStateException("Utility Class");
	}
	
	static Map<String, String> uiMapToServiceAttrib = null;
	
	public static Map<String, String> getServiceMapping(String serviceUrl){
		serviceUrl = serviceUrl.substring(serviceUrl.lastIndexOf('/') + 1, serviceUrl.indexOf('?'));
		
		if(serviceUrl.contains("(")) {
			serviceUrl = serviceUrl.substring(0, serviceUrl.indexOf('('));
		}
		
		switch (serviceUrl.toLowerCase()) {
		case "accountgainloss":
			return getMapping_AccountGainLoss();
		case "accounts":
			return getMapping_Accounts();
		case "accountdetails":
			return getMapping_AccountDetails();
			default:
				return new HashMap<>();
		}
	}
	
	private static Map<String, String> getMapping_AccountGainLoss(){
		uiMapToServiceAttrib = new HashMap<>();
		uiMapToServiceAttrib.put("Account Name", "accountName");
		uiMapToServiceAttrib.put("Account Number", "accountNumber");
		return uiMapToServiceAttrib;
	}
	
	private static Map<String, String> getMapping_Accounts(){
		uiMapToServiceAttrib = new HashMap<>();
		uiMapToServiceAttrib.put("Number", "entityIdNumber");
		uiMapToServiceAttrib.put("Base Currency", "baseCurrency");
		uiMapToServiceAttrib.put("Name", "entityName");
		uiMapToServiceAttrib.put("Date", "postedDate");
		return uiMapToServiceAttrib;
	}
	
	private static Map<String, String> getMapping_AccountDetails(){
		uiMapToServiceAttrib = new HashMap<>();
		uiMapToServiceAttrib.put("Account Number", "entityIdNumber");
		uiMapToServiceAttrib.put("Name", "entityName");
		uiMapToServiceAttrib.put("Opened Date", "openedDate");
		uiMapToServiceAttrib.put("Total Market Value", "marketValue");
		return uiMapToServiceAttrib;
	}
}
