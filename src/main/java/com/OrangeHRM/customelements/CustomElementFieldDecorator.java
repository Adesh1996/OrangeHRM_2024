package com.OrangeHRM.customelements;

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CustomElementFieldDecorator implements FieldDecorator {

	/**
	 * The decorator which is used when a default WebElement is used and the default
	 * behavior can be used.
	 **/
	private final DefaultFieldDecorator defaultFieldDecorator;

	/**
	 * The search context for the (custom) WebElement. Mostly the webDriver.
	 **/
	private final SearchContext searchContext;

	/**
	 * The webDriver
	 **/
	private final WebDriver webDriver;

	/**
	 * The constructor. It constructs.
	 * 
	 * @param searchContext 
	 * 						The search context for the (custom) webelement. Mostly
	 *                      just a webdriver object. Used to find webelements on a
	 *                      webpage.
	 * @param webDriver     
	 * 						The webDriver which will be used to create webelement.
	 **/
	public CustomElementFieldDecorator(SearchContext searchContext, WebDriver webDriver) {
		this.searchContext = searchContext;
		this.webDriver = webDriver;
		defaultFieldDecorator = new DefaultFieldDecorator(new DefaultElementLocatorFactory(searchContext));

	}

	/**
	 * This method is called by the Selenium PageFactory on all fields to decide how
	 * to decorate the field.
	 * 
	 * @param loader 
	 * 				The class loader that was used for the page object.
	 * @param field 
	 * 				 The field which should be decorated. Should be an FindBy
	 *               annotated (custom) webElement.
	 * @return Value to decorate the field with.
	 **/
	@Override
	public Object decorate(ClassLoader loader, Field field) {
		//If it is a custom annotated webelement, then ensure proper
		//initialization via the adding of the callback method
		if (CustomWebElement.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(FindBy.class)) {
			return getEnhancedObject(field.getType(), getElementHandler(field), field.getAnnotation(FindBy.class));
		}
		//If it is a normal webelement, then use the default FieldDecorator implementation
		else {
			return defaultFieldDecorator.decorate(loader, field);
		}
	}

	/**
	 * Creates the class with the callback method. The callback method will be
	 * called when a method is called on the given field object (e.g. a click()
	 * method call on a button).
	 * 
	 * @return The class which contains the callback method.
	 **/
	private CustomElementLocator getElementHandler(Field field) {
		return new CustomElementLocator(getLocator(field));
	}

	/**
	 * Returns the element handler for the field which melds the field and the
	 * locator (aka FindBy) together for further usage. An ElementLocator locator
	 * can find a webelement on a webpage without any parameters since all the
	 * needed information is already there.
	 * 
	 * @param field The annotated field from which the element locator will be
	 *              created.
	 * 
	 * @return The element locator object.
	 **/
	private ElementLocator getLocator(Field field) {
		return new DefaultElementLocatorFactory(searchContext).createLocator(field);
	}

	/**
	 * Enhances the class to call a specific method callback when a method of that
	 * class is called. Example: A button is clicked -> Since the class might be a
	 * custom webelement, the callback method is called to handle the initialization
	 * of the custom webelement.
	 * 
	 * @param clzz              The class which should be enhanced with the callback
	 *                          method (e.g. a custom button). If a method in that
	 *                          class is called, the callback method will be
	 *                          triggered.
	 * @param methodInterceptor The class which implements the callback method.
	 * @param locator           The locator which was used to identify the
	 *                          webelement via the FindBy annotation
	 **/
	private Object getEnhancedObject(Class<?> clzz, MethodInterceptor methodInterceptor, FindBy locator) {
		Enhancer e = new Enhancer();
		WebElementTransformer transformer = new WebElementTransformer();

		e.setSuperclass(clzz);
		e.setCallback(methodInterceptor);

		return e.create(new Class[] { WebDriver.class, By.class },
				new Object[] { webDriver, transformer.transformFindByToBy(locator) });

	}
}
