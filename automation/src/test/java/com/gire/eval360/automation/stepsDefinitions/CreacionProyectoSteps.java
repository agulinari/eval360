package com.gire.eval360.automation.stepsDefinitions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.gire.eval360.automation.domain.Admin;
import com.gire.eval360.automation.domain.Evaluee;
import com.gire.eval360.automation.domain.FeedbackProvider;
import com.gire.eval360.automation.domain.Project;
import com.gire.eval360.automation.domain.Relationship;
import com.gire.eval360.automation.domain.Reviewer;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.annotations.findby.By;

public class CreacionProyectoSteps {
	
	private List<Project> projects = null;
	
	private void waitingForView() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Given("^El usuario crea el proyecto de evaluación$")
	public void el_usuario_crea_el_proyecto_de_evaluación() {

		Admin admin = Admin.builder().idUser(Long.valueOf(2)).creator(Boolean.TRUE).build();
		FeedbackProvider fbPvPjOne = FeedbackProvider.builder().idUser(Long.valueOf(12)).relationship(Relationship.PAR).build(); //ailin
		FeedbackProvider fbPvPjTwo = FeedbackProvider.builder().idUser(Long.valueOf(9)).relationship(Relationship.PAR).build(); //belen
		Reviewer reviewer = Reviewer.builder().idUser(Long.valueOf(6)).build(); //psivori
		Evaluee evalueePjOne = Evaluee.builder().idUser(Long.valueOf(9)).feedbackProvider(fbPvPjOne).reviewer(reviewer).build(); //belen
		Evaluee evalueePjTwo = Evaluee.builder().idUser(Long.valueOf(12)).feedbackProvider(fbPvPjTwo).reviewer(reviewer).build(); //ailin
		Project projectOne = Project.builder().name("Proyecto evaluación uno").description("Creación del primer proyecto").admin(admin).evaluee(evalueePjOne).build();
		Project projectTwo = Project.builder().name("Proyecto evaluación dos").description("Creación del segundo proyecto").admin(admin).evaluee(evalueePjTwo).build();
		projects = Arrays.asList(projectOne, projectTwo);
		
	}

	@When("^El usuario selecciona del menú del dashboard el botón proyectos$")
	public void el_usuario_selecciona_del_menú_del_dashboard_el_botón_proyectos() {
		WebChromeDriver.getDriver().navigate().to("http://localhost:4200/main/project-list");
		waitingForView();		
	}
	
	@When("^El usuario selecciona el botón para dar de alta un nuevo proyecto$")
	public void el_usuario_selecciona_el_botón_para_dar_de_alta_un_nuevo_proyecto() {
		WebElement buttonAdd = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'add')]"));
		buttonAdd.click();
		waitingForView();
	}

	@When("^El usuario selecciona el asistente de creación del proyecto$")
	public void el_usuario_selecciona_el_asistente_de_creación_del_proyecto() {
		WebElement buttonCreate = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Usar Asistente')]"));
		buttonCreate.click();
		waitingForView();
	}

	@When("^El usuario ingresa el nombre del proyecto \"([^\"]*)\" y la descripción \"([^\"]*)\", y deja como único administrador a \"([^\"]*)\"$")
	public void el_usuario_ingresa_el_nombre_del_proyecto_y_la_descripción_y_deja_como_único_administrador_a(String arg1, String arg2, String arg3) {
		WebElement nombreProyecto = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Nombre del Proyecto']"));
	    WebElement descripcionProyecto = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Descripción']"));
	    String nameAdmin = WebChromeDriver.getDriver().findElement(By.className("avatar")).getAttribute("alt");
	    
	    nombreProyecto.sendKeys(arg1);
	    descripcionProyecto.sendKeys(arg2);
	    assertEquals(arg3, nameAdmin);
	    
	    waitingForView();
	}

	@When("^El usuario hace click en siguiente$")
	public void el_usuario_hace_click_en_siguiente() {
		WebElement buttonSiguiente = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Siguiente')]"));
		buttonSiguiente.click();
		waitingForView();
	}

	@When("^El usuario selecciona el botón de agregar evaluado$")
	public void el_usuario_selecciona_el_botón_de_agregar_evaluado() {
		WebElement buttonAgregarEval = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Agregar Evaluado')]"));
		buttonAgregarEval.click();
		waitingForView();
	}

	@When("^El usuario ingresa el nombre del evaluado \"([^\"]*)\" y lo selecciona$")
	public void el_usuario_ingresa_el_nombre_del_evaluado_y_lo_selecciona(String arg1) {
		WebElement evaluadoProy = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Selecciona una persona a evaluar']"));
	    evaluadoProy.sendKeys(arg1);
	    waitingForView();
	    evaluadoProy.sendKeys(Keys.ARROW_DOWN);
	    evaluadoProy.sendKeys(Keys.ENTER);
	    waitingForView();
	}

	@When("^El usuario selecciona el botón de agregar evaluador$")
	public void el_usuario_selecciona_el_botón_de_agregar_evaluador() {
		WebElement buttonAgregarEvaluador = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Agregar Evaluador')]"));
		buttonAgregarEvaluador.click();
		waitingForView();
	}

	@When("^El usuario ingresa el nombre del evaluador \"([^\"]*)\" y lo selecciona$")
	public void el_usuario_ingresa_el_nombre_del_evaluador_y_lo_selecciona(String arg1) {
		WebElement evaluadorProy = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Selecciona un evaluador']"));
	    evaluadorProy.sendKeys(arg1);
	    waitingForView();
	    evaluadorProy.sendKeys(Keys.ARROW_DOWN);
	    evaluadorProy.sendKeys(Keys.ENTER);
	    waitingForView();
	}

	@When("^El usuario selecciona la relación de \"([^\"]*)\" que tiene el evaluador con el evaluado$")
	public void el_usuario_selecciona_la_relación_de_que_tiene_el_evaluador_con_el_evaluado(String arg1) {
		WebElement relacionEvaluadorConEvaluado = WebChromeDriver.getDriver().findElement(By.xpath("//mat-select[@placeholder='Relacion']"));
		relacionEvaluadorConEvaluado.sendKeys(arg1);
	    waitingForView();
	}

	@When("^El usuario selecciona el botón de agregar reviewer$")
	public void el_usuario_selecciona_el_botón_de_agregar_reviewer() {
		WebElement buttonAgregarReviewer = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Agregar Reviewer')]"));
		buttonAgregarReviewer.click();
		waitingForView();
	}

	@When("^El usuario ingresa el nombre del reviewer \"([^\"]*)\" y lo selecciona$")
	public void el_usuario_ingresa_el_nombre_del_reviewer_y_lo_selecciona(String arg1) {
		WebElement reviewerProy = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Selecciona un reviewer']"));
		reviewerProy.sendKeys(arg1);
	    waitingForView();
	    reviewerProy.sendKeys(Keys.ARROW_DOWN);
	    reviewerProy.sendKeys(Keys.ENTER);
	    waitingForView();
	}

	@When("^El usuario hace click en el botón Ok$")
	public void el_usuario_hace_click_en_el_botón_Ok() {
		WebElement buttonOk = WebChromeDriver.getDriver().findElement(By.xpath("//button[contains(.,'Ok')]"));
		buttonOk.click();
		waitingForView();
	}

	@When("^El usuario hace click en siguiente para elegir el template$")
	public void el_usuario_hace_click_en_siguiente_para_elegir_el_template() {
		WebElement buttonSiguiente = WebChromeDriver.getDriver().findElement(By.id("nextStepOne"));
		buttonSiguiente.click();
		waitingForView();
	}
	 
	@When("^El usuario ingresa el nombre del template \"([^\"]*)\" para la evaluación y lo selecciona$")
	public void el_usuario_ingresa_el_nombre_del_template_para_la_evaluación_y_lo_selecciona(String arg1) {
		WebElement templateProy = WebChromeDriver.getDriver().findElement(By.xpath("//input[@placeholder='Selecciona un template de evaluación']"));
		templateProy.sendKeys(arg1);
	    waitingForView();
	    templateProy.sendKeys(Keys.ARROW_DOWN);
	    templateProy.sendKeys(Keys.ENTER);
	    waitingForView();
	}
	 
	@When("^El usuario hace click en siguiente para finalizar la creación del proyecto$")
	public void el_usuario_hace_click_en_siguiente_para_finalizar_la_creación_del_proyecto() {
		WebElement buttonSiguiente = WebChromeDriver.getDriver().findElement(By.id("nextStepTwo"));
		buttonSiguiente.click();
		waitingForView();
	}

	@When("^El usuario hace click en el botón finalizar$")
	public void el_usuario_hace_click_en_el_botón_finalizar() {
		WebElement buttonFinalizar = WebChromeDriver.getDriver().findElement(By.id("stepFin"));
		buttonFinalizar.click();
		waitingForView();
	}

	@Then("^Verifico que el proyecto creado este en estado \"([^\"]*)\"$")
	public void verifico_que_el_proyecto_creado_este_en_estado(String arg1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Then("^El proyecto tenga asignado el nombre \"([^\"]*)\", la descripcion \"([^\"]*)\" y el único administrador \"([^\"]*)\"$")
	public void el_proyecto_tenga_asignado_el_nombre_la_descripcion_y_el_único_administrador(String arg1, String arg2, String arg3) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Then("^El proyecto tenga asignado el evaluado \"([^\"]*)\", el evaluador \"([^\"]*)\", y el reviewer \"([^\"]*)\"$")
	public void el_proyecto_tenga_asignado_el_evaluado_el_evaluador_y_el_reviewer(String arg1, String arg2, String arg3) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Then("^El proyecto tenga asignado el template de evaluación \"([^\"]*)\"$")
	public void el_proyecto_tenga_asignado_el_template_de_evaluación(String arg1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}


}
