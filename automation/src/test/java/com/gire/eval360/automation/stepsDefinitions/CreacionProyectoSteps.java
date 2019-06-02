package com.gire.eval360.automation.stepsDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreacionProyectoSteps {
		
	@Given("^Quiero crear un proyecto con nombre Proyecto evaluación (\\d+) y descripción Creación del primer proyecto$")
	public void quiero_crear_un_proyecto_con_nombre_Proyecto_evaluación_y_descripción_Creación_del_primer_proyecto(int arg1) {
	   try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}


	@When("^El usuario creador del proyecto hace click en el botón finalizar$")
	public void el_usuario_creador_del_proyecto_hace_click_en_el_botón_finalizar() {
		 try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Then("^Verifico que el proyecto este en estado PENDIENTE, y tenga el nombre Proyecto evaluación (\\d+) y descripción Creación del primer proyecto$")
	public void verifico_que_el_proyecto_este_en_estado_PENDIENTE_y_tenga_el_nombre_Proyecto_evaluación_y_descripción_Creación_del_primer_proyecto(int arg1) {
		 try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
