#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Creacion de proyecto
  Creación de proyecto utilizando el asistente de creación
		 
  @tag1
  Scenario Outline: Creación de proyectos con nombre y descripción
    Given El usuario crea el proyecto de evaluación
    When El usuario selecciona del menú del dashboard el botón proyectos
    And El usuario selecciona el botón para dar de alta un nuevo proyecto
    And El usuario selecciona el asistente de creación del proyecto
    And El usuario ingresa el nombre del proyecto "<nombre_proyecto>" y la descripción "<descripcion_proyecto>", y deja como único administrador a "<nombre_administrador>"
    And El usuario hace click en siguiente 
    And El usuario selecciona el botón de agregar evaluado
    And El usuario ingresa el nombre del evaluado "<nombre_evaluado>" y lo selecciona
    And El usuario selecciona el botón de agregar evaluador
	And El usuario ingresa el nombre del evaluador "<nombre_evaluador>" y lo selecciona
	And El usuario selecciona la relación de "<relacion_con_evaluado>" que tiene el evaluador con el evaluado
	And El usuario selecciona el botón de agregar reviewer
	And El usuario ingresa el nombre del reviewer "<nombre_reviewer>" y lo selecciona
	And El usuario hace click en el botón Ok
	And El usuario hace click en siguiente para elegir el template
	And El usuario ingresa el nombre del template "<nombre_template>" para la evaluación y lo selecciona
	And El usuario hace click en siguiente para finalizar la creación del proyecto
	And El usuario hace click en el botón finalizar
    Then Verifico que el proyecto creado este en estado "<estado_proyecto>"
    And El proyecto tenga asignado el nombre "<nombre_proyecto>", la descripcion "<descripcion_proyecto>" y el único administrador "<nombre_administrador>" 
    And El proyecto tenga asignado el evaluado "<nombre_evaluado>", el evaluador "<nombre_evaluador>", y el reviewer "<nombre_reviewer>"
    And El proyecto tenga asignado el template de evaluación "<nombre_template>"

    Examples: 
      | nombre_proyecto  | descripcion_proyecto | nombre_administrador | nombre_evaluado | nombre_evaluador | relacion_con_evaluado | nombre_reviewer | nombre_template | estado_proyecto |
      | Proyecto evaluación uno | Creación del primer proyecto | admin | belen | ailin | PAR | psivori | Evaluación 360 | PENDIENTE |
      | Proyecto evaluación dos | Creación del segundo proyecto | admin | ailin | belen | PAR | psivori | Evaluación 360 | PENDIENTE |
