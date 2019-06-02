package com.gire.eval360.automation.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features="src/test/resources/features",glue="com/gire/eval360/automation/stepsDefinitions")
public class RunTest {

}
