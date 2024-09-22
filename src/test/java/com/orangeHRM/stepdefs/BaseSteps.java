package com.orangeHRM.stepdefs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.OrangeHRM.browsers.LocalDriver;
import com.OrangeHRM.customelements.OrangeGridElement;
import com.OrangeHRM.customelements.OrangeTableElement;
import com.OrangeHRM.customelements.OrangeWebElement;
import com.OrangeHRM.customelements.OrangeWidgetElement;
import com.OrangeHRM.utilities.OrangeUtils;
import com.OrangeHRM.utilities.ScenerioDataProvider;

import io.cucumber.java.Scenario;

public class BaseSteps {

	protected static ThreadLocal<Scenario> scenario = new ThreadLocal<>();
	protected static ThreadLocal<Map<String, String>> scenarioData = new ThreadLocal<>();
	protected static ThreadLocal<String> parentWindowHandle = new ThreadLocal<>();
	String basePagePackage = "com.OrangeHRM.pages.";

	public void setScenerio(Scenario sc) {
		scenario.set(sc);
		scenarioData.set(ScenerioDataProvider.getData(scenario.get().getName()));
	}

	public OrangeWebElement getWebElement(String locator, WebDriver driver) {
		OrangeWebElement element = null;
		if (locator != null && driver != null) {
			StringTokenizer tokenizer = new StringTokenizer(locator, ".");
			List<String> tokenList = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				tokenList.add(tokenizer.nextToken());
			}
			Class<?> clazz = getClassObject(tokenList.get(0));
			element = (OrangeWebElement) getClassVariable(driver, clazz, tokenList.get(1));
		}
		return element;
	}

	public OrangeGridElement getGridElement(String locator, WebDriver driver) {
		OrangeGridElement element = null;
		if (locator != null && driver != null) {
			StringTokenizer tokenizer = new StringTokenizer(locator, ".");
			List<String> tokenList = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				tokenList.add(tokenizer.nextToken());
			}
			Class<?> clazz = getClassObject(tokenList.get(0));
			element = (OrangeGridElement) getClassVariable(driver, clazz, tokenList.get(1));
		}
		return element;
	}
	
	
	public OrangeTableElement getTableElement(String locator, WebDriver driver) {
		OrangeTableElement element = null;
		if (locator != null && driver != null) {
			StringTokenizer tokenizer = new StringTokenizer(locator, ".");
			List<String> tokenList = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				tokenList.add(tokenizer.nextToken());
			}
			Class<?> clazz = getClassObject(tokenList.get(0));
			element = (OrangeTableElement) getClassVariable(driver, clazz, tokenList.get(1));
		}
		return element;
	}
	
	public OrangeWidgetElement getWidgetElement(String locator, WebDriver driver) {
		OrangeWidgetElement element = null;
		if (locator != null && driver != null) {
			StringTokenizer tokenizer = new StringTokenizer(locator, ".");
			List<String> tokenList = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				tokenList.add(tokenizer.nextToken());
			}
			Class<?> clazz = getClassObject(tokenList.get(0));
			element = (OrangeWidgetElement) getClassVariable(driver, clazz, tokenList.get(1));
		}
		return element;
	}

	/**
	 * Returns reference object of the given className
	 * 
	 * @param className
	 * @return
	 */
	public Class<?> getClassObject(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(basePagePackage + className);
		} catch (ClassNotFoundException e) {
			Assert.fail("Given page class is not found :  " + className);
		} catch (Exception e) {
			Assert.fail("", e.getCause());
		}
		return clazz;
	}

	/**
	 * Returns reference object of the given class variable
	 * 
	 * @param clazz
	 * @param variableName
	 * @return
	 */

	public Object getClassVariable(WebDriver driver, Class<?> clazz, String variableName) {
		Field field = null;
		Object fieldObject = null;

		try {
			Constructor <?> constructor = clazz.getConstructor(WebDriver.class);
			Object obj = constructor.newInstance(driver);

			field = clazz.getDeclaredField(variableName);
			// field.setAccessible(true);
			fieldObject = field.get(obj);
		} catch (NoSuchMethodException e) {
			Assert.fail("Constructor is not defined for the given page class : " + clazz.getSimpleName());
		} catch (NoSuchFieldException e) {
			Assert.fail("Field with name " + variableName + " not defined for the given page class  : "
					+ clazz.getSimpleName());
		} catch (Exception e) {
			Assert.fail(" ", e.getCause());
		}

		return fieldObject;
	}

	/**
	 * Invoke given method of a class
	 */
	public void invokeMethod(WebDriver driver, Class<?> clazz, String methodName) {
		try {
			Constructor <?> constructor = clazz.getConstructor(WebDriver.class);
			Object obj = constructor.newInstance(driver);
			Method method = clazz.getDeclaredMethod(methodName);
			method.invoke(obj);
		}
		catch(NoSuchMethodException e ) {
			Assert.fail("Method is not defined for the given page class : " + clazz.getSimpleName());
		}
		catch(IllegalAccessException e) {
			Assert.fail("Method " + methodName + " is not accessible. Please declare method as public");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected String getVariableValue(String variableName) {
		String variableValue = variableName;
		if(variableName.contains("Service")) {
			variableValue = scenarioData.get().get(variableName.replace("#", "")).trim().replace("userName", 
					scenarioData.get().get("UserID").toLowerCase());
		}else if (variableName.startsWith("#")) {
			variableValue = scenarioData.get().get(variableName.replace("#", "")).trim();
		}
		Assert.assertFalse(variableValue == null,
				"Key with name " + variableName  + " ' is either incorrect/not set or not present in test data sheet");
		return variableValue;
	}
	
	protected String getPageClassName(String loctor) {
		if(loctor.contains(".")) {
			return loctor.substring(0, loctor.indexOf("."));
		}else
			return loctor;
	}
	
	protected void waitForPageToLoad(String pageClassName) {
		Class<?> clazz = getClassObject(pageClassName);
		invokeMethod(LocalDriver.getDriver(), clazz, "waitForPageToLoad");
		OrangeUtils.wait(2000);
	}
}
