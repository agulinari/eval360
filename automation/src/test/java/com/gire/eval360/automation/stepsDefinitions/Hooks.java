package com.gire.eval360.automation.stepsDefinitions;

import org.openqa.selenium.WebElement;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import net.serenitybdd.core.annotations.findby.By;

public class Hooks {

	@Before(order=1)
	public void beforeScenario(){
		System.out.println("Start the browser");
		WebElement usuario = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Usuario']"));
	    WebElement password = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Password']"));
	    usuario.sendKeys("admin");
	    password.sendKeys("admin1234");
	    usuario.submit();
	    password.submit();
	    WebChromeDriver.getDriver().navigate().forward();
	    try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	@Before(order=0)
	public void beforeScenarioStart(){
		System.out.println("-----------------Start of Scenario-----------------");
		WebChromeDriver.initialize();
		
	} 

	@After(order=0)
	public void afterScenarioFinish(){
		System.out.println("-----------------End of Scenario-----------------");	
		WebChromeDriver.getDriver().quit();
	} 
	
	@After(order=1)
	public void afterScenario(){
		System.out.println("Log out the user and close the browser");
	} 

}
