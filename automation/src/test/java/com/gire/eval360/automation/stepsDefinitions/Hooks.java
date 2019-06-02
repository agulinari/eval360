package com.gire.eval360.automation.stepsDefinitions;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import net.serenitybdd.core.annotations.findby.By;

public class Hooks {

	WebDriver driver = null;
	
	private void setup() {
		System.setProperty("webdriver.chrome.driver", "src/test/driver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
	    options.addArguments("-incognito");
		driver = new ChromeDriver(options); 
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://www.google.com/xhtml");
		driver.navigate().to("localhost:4200");
	}
	
	@Before(order=1)
	public void beforeScenario(){
		System.out.println("Start the browser");
		WebElement usuario = driver.findElement(By.xpath("//input[@placeholder='Usuario']"));
	    WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
	    usuario.sendKeys("admin");
	    password.sendKeys("admin1234");
	    usuario.submit();
	    password.submit();
	    driver.navigate().forward();
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
		setup();
		
	} 

	@After(order=0)
	public void afterScenarioFinish(){
		System.out.println("-----------------End of Scenario-----------------");	
		driver.quit();
	} 
	
	@After(order=1)
	public void afterScenario(){
		System.out.println("Log out the user and close the browser");
	} 

}
