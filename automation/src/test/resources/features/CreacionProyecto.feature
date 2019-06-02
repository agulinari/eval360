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
    Given Quiero crear un proyecto con nombre <nombre> y descripción <descripcion>
    When El usuario creador del proyecto hace click en el botón finalizar
    Then Verifico que el proyecto este en estado <estado>, y tenga el nombre <nombre> y descripción <descripcion>

    Examples: 
      | nombre  | descripcion | estado |
      | Proyecto evaluación 1 | Creación del primer proyecto | PENDIENTE |
