package com.OrangeHRM.utilities;

import static io.restassured.RestAssured.get;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;

import com.OrangeHRM.constants.AppConstants;

import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.path.json.config.JsonPathConfig.NumberReturnType;
import io.restassured.response.Response;

public class ServiceUtilities {

	/**
	 * This constructor is used to set base url and port of the service
	 */
	public ServiceUtilities() {
		switch (AppConstants.APPLICATION_ENV.toLowerCase()) {
		case "orangehrm":
			RestAssured.baseURI = AppConstants.SERVICE_BASE_URL_ORANGEHRM;
			RestAssured.port = AppConstants.SERVICE_PORT_ORANGEHRM;
			break;
		case "orangehrmuat":
			RestAssured.baseURI = AppConstants.SERVICE_BASE_URL_ORANGEHRMUAT;
			RestAssured.port = AppConstants.SERVICE_PORT_ORANGEHRMUAT;
			break;
		default:
			Assert.fail("*****Web service environment is not properly set*****");
			break;
		}
		RestAssured.urlEncodingEnabled = false;
	}

	public Map<String, String> getServiceResponse(String resourceUrl) {
		Response response = get(resourceUrl);
		String responseBody = response.asString();
		return ParseJson.jsonToMap_old(responseBody);
	}

	public Map<String, String> getBasicServiceResponse(String resourceUrl) {
		Map<String, String> responseMap = new HashMap<>();
		Response response = get(resourceUrl);
		String responseBody = response.asString();

		if (!responseBody.contains("value")) {
			responseBody = responseBody.substring(responseBody.indexOf('[') + 1, responseBody.indexOf(']'));
			responseMap = ParseJson.jsonToMap(responseBody);
		} else if (responseBody.contains("[")) {
			responseBody = responseBody.substring(responseBody.indexOf('[') + 1, responseBody.lastIndexOf(']'));
			List<String> list = Arrays.asList(responseBody.split("},"));
			for (int i = 0; i < list.size(); i++) {
				responseMap = ParseJson.jsonToMap(list.get(i));
			}
		} else {
			JsonPathConfig jsonPathConfig = new JsonPathConfig(NumberReturnType.BIG_DECIMAL);
			responseMap = response.jsonPath(jsonPathConfig).getMap("value");
		}
		return responseMap;
	}

	public Map<String, List<String>> getComplexServiceResponse(String resourceUrl) {
		Map<String, List<String>> responseMap = new HashMap<>();
		Map<String, String> tempResponseMap;
		List<String> tempList = new ArrayList<>();
		List<String> formattedList = null;
		String key = "";
		String value = "";
		Response response = get(resourceUrl);
		Assert.assertEquals(response.getStatusCode(), 200, "!! Invalid Service URL !!\n" + resourceUrl + "\n");
		String responseBody = response.asString();
		responseBody = responseBody.substring(responseBody.indexOf('[') + 1, responseBody.lastIndexOf(']'));
		List<String> list = Arrays.asList(responseBody.split("},"));

		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				tempResponseMap = ParseJson.jsonToMap(list.get(i) + "}");
			} else {
				tempResponseMap = ParseJson.jsonToMap(list.get(i));
			}
			Assert.assertFalse(tempResponseMap.isEmpty(), "Service returned blank response");

			for (Map.Entry<String, String> entry : tempResponseMap.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				if (value != null) {
					if (!responseMap.containsKey(entry.getKey())) {
						if (entry.getKey().equalsIgnoreCase("modifiedDate")
								|| entry.getKey().equalsIgnoreCase("startDateTime")) {
							if (value.contains("T00:00:00") || value.contains("T") || value.contains("T0")) {
								value = value.substring(0, value.indexOf('T'));
								Date date;
								try {
									date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
									SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
									value = form.format(date);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							tempList.add(value.trim());
							formattedList = formatServiceResponseValues(tempList);
							responseMap.put(key, formattedList);
							tempList = new ArrayList<>();
						} else {
							tempList.add(value.trim());
							formattedList = formatServiceResponseValues(tempList);
							responseMap.put(key, formattedList);
							tempList = new ArrayList<>();
						}

					} else if (entry.getKey().equalsIgnoreCase("modifiedDate")
							|| entry.getKey().equalsIgnoreCase("startDateTime")) {
						if (value.contains("T00:00:00") || value.contains("T") || value.contains("T0")) {
							value = value.substring(0, value.indexOf('T'));
							Date date;
							try {
								date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
								SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
								value = form.format(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							tempList.add(value.trim());
							formattedList = formatServiceResponseValues(tempList);
							responseMap.get(key).addAll(formattedList);
							tempList = new ArrayList<>();
						} else {
							tempList.add(value.trim());
							formattedList = formatServiceResponseValues(tempList);
							responseMap.get(key).addAll(formattedList);
							tempList = new ArrayList<>();
						}
					} else {
						if (!responseMap.containsKey(entry.getKey())) {
							tempList.add("");
							responseMap.put(key, tempList);
							tempList = new ArrayList<>();
						} else {
							tempList.add("");
							responseMap.get(key).addAll(tempList);
							tempList = new ArrayList<>();
						}
					}
				}
			}

		}
		return responseMap;
	}

	private boolean responseContainJsonArrayChecker(String responseBody) {
		String patternString = ".*\\[.*\\].*";

		Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);

		Matcher matcher = pattern.matcher(responseBody);
		return matcher.matches();
	}

	public Map<Integer, List<Map<String, String>>> getCommplexServiceResponseForJsonArray(String resourceUrl,
			String data) {
		Map<Integer, List<Map<String, String>>> responseMap = new HashMap<>();
		Response response = get(resourceUrl);
		Assert.assertEquals(response.getStatusCode(), 200, "!! Invalid Service URL !! \n" + resourceUrl + "\n");
		String responseBody = response.asString();

		responseBody = responseBody.substring(responseBody.indexOf('[') + 1, responseBody.lastIndexOf(']'));
		List<String> list = new ArrayList<>();

		if (responseContainJsonArrayChecker(responseBody)) {
			list = Arrays.asList(responseBody.split("]\r\n   },"));
			for (int i = 0; i < list.size(); i++) {
				responseMap.put(i, ParseJson.getJsonArrayToListOfMap(list.get(i) + "]\r\n   }", data));
			}
		}
		return responseMap;
	}

	public Map<String, List<String>> getComplexServiceResponseforNestedJsonArray(String resourceUrl) {
		Map<String, List<String>> responseMap = new HashMap<>();
		Map<String, Object> tempResponseMap = null;
		List<String> tempList = new ArrayList<>();
		List<String> formattedList = null;
		String key = "";
		List<String> values = new ArrayList<>();
		Response response = get(resourceUrl);

		Assert.assertEquals(response.getStatusCode(), 200, "!! Invalid Service URL !! \n" + resourceUrl + "\n");
		String responseBody = response.asString();

		responseBody = responseBody.substring(responseBody.indexOf('[') + 1, responseBody.lastIndexOf(']'));
		List<String> list = new ArrayList<>();

		if (responseContainJsonArrayChecker(responseBody)) {
			list = Arrays.asList(responseBody.split("]\r\n   },"));
		} else {
			list = Arrays.asList(responseBody.split("},"));
		}

		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1 && responseContainJsonArrayChecker(responseBody)) {
				tempResponseMap = ParseJson.jsonToMap_new(list.get(i) + "]\r\n   }");
			} else if (i < list.size() - 1 && !responseContainJsonArrayChecker(responseBody)) {
				tempResponseMap = ParseJson.jsonToMap_new(list.get(i) + "}");
			} else {
				tempResponseMap = ParseJson.jsonToMap_new(list.get(i));
			}

			for (Entry<String, Object> entry : tempResponseMap.entrySet()) {
				key = entry.getKey();
				values.add(entry.getValue().toString());

				if (!values.isEmpty())
					if (!responseMap.containsKey(entry.getKey())) {
						tempList.addAll(values);
						formattedList = formatServiceResponseValues(tempList);
						responseMap.put(key, formattedList);
						tempList = new ArrayList<>();
					} else {
						tempList.addAll(values);
						formattedList = formatServiceResponseValues(tempList);
						responseMap.get(key).addAll(formattedList);
						tempList = new ArrayList<>();
					}
				values.clear();

			}
		}
		return responseMap;
	}

	/**
	 * This method formats the JSON response values to be able to match with UI
	 * values and returns it
	 * 
	 * @param responseMap - Map of raw JSON response
	 * @return
	 * @return
	 */
	public Map<String, String> formatResponseMap(String resourceUrl) {
		Map<String, String> responseMap = getBasicServiceResponse(resourceUrl);
		Map<String, String> formatResponseMap = new HashMap<>();
		String key = null;
		String value = null;
		try {
			for (Map.Entry<String, String> entry : responseMap.entrySet()) {
				key = entry.getKey();
				value = entry.toString().substring(entry.toString().indexOf('=') + 1);
				if (value.endsWith(".00")) {
					value = value.replace(".00", "").trim();
				} else if (value.endsWith(".0")) {
					value = value.replace(".0", "").trim();
				} else if (value.equalsIgnoreCase("false")) {
					value = "No";
				} else if (value.equalsIgnoreCase("true")) {
					value = "Yes";
				} else if ((key.toLowerCase().contains("date") || key.toLowerCase().contains("year"))
						&& value.contains("T") || value.contains("T00:")) {
					value = value.substring(0, value.indexOf('T'));
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
					SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
					value = form.format(date);
				}

				formatResponseMap.put(key, value);
			}
			return formatResponseMap;
		} catch (ParseException e) {
			Assert.fail(" ", e.getCause());
			return null;
		}

	}

	public Map<String, String> formatResponseMap(Map<String, String> responseMap) {
		Map<String, String> formatResponseMap = new HashMap<>();
		String key = null;
		String value = null;
		try {
			for (Map.Entry<String, String> entry : responseMap.entrySet()) {
				key = entry.getKey();
				value = entry.toString().substring(entry.toString().indexOf('=') + 1);
				if (value.endsWith(".00")) {
					value = value.replace(".00", "").trim();
				} else if (value.endsWith(".0")) {
					value = value.replace(".0", "").trim();
				} else if (value.contains("  ")) {
					value = value.replace("  ", "").trim();
				} else if (value.equalsIgnoreCase("false")) {
					value = "No";
				} else if (value.equalsIgnoreCase("true")) {
					value = "Yes";
				} else if (key.toLowerCase().contains("date") && value.contains("T")) {
					value = value.substring(0, value.indexOf('T'));
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
					SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
					value = form.format(date);
				}

				if (value.contains(".")) {
					try {
						BigDecimal decimal = new BigDecimal(value).stripTrailingZeros();
						value = decimal.toPlainString();

					} catch (NumberFormatException e) {
						// Do nothing if uiValues is not a number
					}
				}
				formatResponseMap.put(key, value);
			}
			return formatResponseMap;

		} catch (ParseException e) {
			Assert.fail("", e.getCause());
			return null;
		}

	}

	public List<String> formatServiceResponseValues(List<String> uiValues) {
		List<String> formattedUIValues = new ArrayList<>();
		try {
			for (String value : uiValues) {
				if (value.contains(".00")) {
					value = value.replace(".00", "").trim();
				} else if (value.contains(".")) {
					try {
						BigDecimal decimal = new BigDecimal(value).stripTrailingZeros();
						value = decimal.toPlainString();
					} catch (NumberFormatException e) {
						// Do nothing if value is not a valid number
					}
				} else if (value.equalsIgnoreCase("false")) {
					value = "No";
				} else if (value.equalsIgnoreCase("true")) {
					value = "Yes";
				} else if (value.contains("T00:00:00")) {
					value = value.substring(0, value.indexOf('T'));
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
					SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
					value = form.format(date);
				} else if (value.contains("|")) {
					value = value.replaceAll("\\|", "\n").trim();
				}
				if (value.contains(",")) {
					value = value.replaceAll(",", "").trim();
				}
				if (value.contains("  ")) {
					value = value.replaceAll("  ", " ").trim();
				}
				if (value.toString().contains(" ")) {
					value = value.replaceAll("\\s+", " ").trim();
				}
				if (value.endsWith("-%")) {
					value.replace("-%", "");
				}
				if (value.endsWith("%")) {
					value.replaceAll("%", "").trim();
				}
				if (value.contains("%")) {
					value = value.replaceAll("%", "").trim();
				}
				formattedUIValues.add(value);
			}
		} catch (ParseException e) {
			Assert.fail(e.getLocalizedMessage());
		}
		return formattedUIValues;
	}

	/**
	 * This generic method returns all values of a given service attribute from the
	 * service response in form of a list
	 * 
	 * @param serviceUrl    - URL of the service
	 * @param attributeName - name of the service attribute
	 * @return - List of values for the given attribute
	 */

	public List<String> getServiceAttributeValues(String serviceUrl, String attributeName) {
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		Map<String, List<String>> responseMap = serviceUtilities.getComplexServiceResponse(serviceUrl);
		return responseMap.get(attributeName);
	}

	public String getResultSetId(String serviceUrl) {
		Response response = get(serviceUrl);
		String responseBody = response.asString();
		int indexOfResultSet = responseBody.indexOf("resultSetId") + 14;
		return responseBody.substring(indexOfResultSet, responseBody.indexOf(',', indexOfResultSet));
	}
	
	public List<Map<String, String>> getServiceAttributeValuesFromJsonArray(String serviceUrl, String attributeName) {
		ServiceUtilities serviceUtilities = new ServiceUtilities();
		
		Map<Integer, List<Map<String, String>>> responseMap  =  serviceUtilities.getCommplexServiceResponseForJsonArray(serviceUrl, attributeName);
		return responseMap.get(0);
		}
	

}
