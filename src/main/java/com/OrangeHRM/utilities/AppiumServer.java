package com.OrangeHRM.utilities;

import java.io.IOException;
import java.net.ServerSocket;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumServer {
	
	private AppiumDriverLocalService service;
	
	public void startServer() {
		DesiredCapabilities capabilities;
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("noReset", "true");
		
		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(4723);
		builder.withCapabilities(capabilities);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}
	
	public void stopServer() {
		service.stop();
	}
	
	public boolean checkIfServerIsRunning(int port) {
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (IOException e) {
		//If control comes here, then it means that the port is in use
			isServerRunning =true;
		}
		return isServerRunning;
	}
}
