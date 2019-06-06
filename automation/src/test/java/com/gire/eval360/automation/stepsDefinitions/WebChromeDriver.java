package com.gire.eval360.automation.stepsDefinitions;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebChromeDriver {

	static ChromeDriver driver = null;
	
	public static ChromeDriver getDriver() {
		return driver;
	}
	
	public static void initialize()
	{
		System.setProperty("webdriver.chrome.driver", "src/test/driver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
	    options.addArguments("-incognito");
		driver = new ChromeDriver(options); 
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://www.google.com/xhtml");
		driver.navigate().to("localhost:4200");
	}
	
}
