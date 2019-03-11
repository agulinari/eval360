package com.gire.eval360.projects.domain.request.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gire.eval360.projects.domain.request.sheet.FieldProject;
import com.gire.eval360.projects.domain.request.sheet.FieldSheet;
import com.gire.eval360.projects.domain.request.sheet.FieldUser;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class HeaderSheetValidator{
	
	private static final String FIELD_NAME = "nombre";
	private static final String FIELD_DESCRIPTION = "descripción";
	private static final String FIELD_USERNAME = "nombre de usuario";
//	private static final String FIELD_ROL = "rol";
	private static final String FIELD_RELATIONSHIP = "relación";
	private static final String FIELD_EVALUEE = "evaluado";
	
	/** ROLES **/
	private static final String FEEDBACK_PROVIDER_ROL = "FP";
	private static final String PROJECT_ADMIN_ROL = "PA";
	private static final String REVIEWER_ROL = "RV";
	
	/** ROL VALID **/
	private static final List<String> ROL_VALID = Arrays.asList(FEEDBACK_PROVIDER_ROL,PROJECT_ADMIN_ROL,REVIEWER_ROL);
		
	private Validation<String, String> fieldIsNotBlank(String field,String fieldName) {
		return (field!=null && !field.isEmpty())?Validation.valid(field):Validation.invalid("El campo "+fieldName+" no puede ser vacio o nulo.");
	}
	
	private Validation<String, String> fieldIsBlank(String field,String fieldName) {
		return (field==null || field.isEmpty())?Validation.valid(field):Validation.invalid("El campo "+fieldName+" debe ser vacio o nulo.");
	}

	private Validation<String,String> fieldRolValid(String field){
		return (ROL_VALID.contains(field))?Validation.valid(field):Validation.invalid("El rol "+field+" no es valido. Los roles validos son: "+ROL_VALID.toString());
	}
	
	private Validation<String,String> fieldUserNameValid(String field){
		return fieldIsNotBlank(field,FIELD_USERNAME);
	}
	
	private Validation<String,String> fieldRelationShipValid(String fieldRelationShip, String fieldRol){
		
		if(fieldRol!=null && (fieldRol.equalsIgnoreCase(PROJECT_ADMIN_ROL)||fieldRol.equalsIgnoreCase(REVIEWER_ROL))) {
			return fieldIsBlank(fieldRelationShip,FIELD_RELATIONSHIP);
		}
		
		return fieldIsNotBlank(fieldRelationShip,FIELD_RELATIONSHIP);
	}
	
	private Validation<String,String> fieldEvalueeValid(String fieldEvaluee,String fieldRol){
		
		if(fieldRol!=null && fieldRol.equalsIgnoreCase(PROJECT_ADMIN_ROL)) {
			return fieldIsBlank(fieldEvaluee,FIELD_EVALUEE);
		}
		
		return fieldIsNotBlank(fieldEvaluee,FIELD_RELATIONSHIP);
		
	}
	
	private Validation<String, Void> countColumnsIsValid(List<String> row){
		return (row.isEmpty()||row.size()<4)?Validation.invalid("Faltan datos en usuarios"):null;
	}
			
	private Validation<Seq<String>, FieldUser> rowUserIsValid(List<String> row) {
		
		Validation<String,Void> valColumns = countColumnsIsValid(row);
		
		if(valColumns.isValid()) {	
			String userName = row.get(0);
			String rol = row.get(1);
			String relationShip = row.get(2);
			String userNameEvaluee = row.get(3);
			return Validation.combine(fieldUserNameValid(userName),fieldRolValid(rol),fieldRelationShipValid(relationShip,rol),
									  fieldEvalueeValid(userNameEvaluee, rol)).ap(FieldUser::new);
		}
		
		return Validation.invalid(io.vavr.collection.List.of(valColumns.getError()));
		
	}
	
	private List<Validation<Seq<String>,FieldUser>> fieldUserIsNotValid(List<List<String>> users){
		return users.stream().map(row->rowUserIsValid(row)).collect(Collectors.toList());
	}
	
	private Validation<Seq<String>,FieldProject> validateFieldProject(String nameProject, String descProject, String nameTemplate){
		return Validation.combine(fieldIsNotBlank(nameProject, FIELD_NAME),fieldIsNotBlank(descProject,FIELD_DESCRIPTION),fieldIsNotBlank(nameTemplate,FIELD_DESCRIPTION)).
			   ap(FieldProject::new);
	}
		
	public List<Validation<Seq<Seq<String>>, FieldSheet>> validateProject(String nameProject, String descProject, String nameTemplate, List<List<String>> users) {
		return fieldUserIsNotValid(users).stream().map(val->Validation.combine(validateFieldProject(nameProject, descProject, nameTemplate),val).ap(FieldSheet::new)).
												   collect(Collectors.toList());
									
	}
	
}
