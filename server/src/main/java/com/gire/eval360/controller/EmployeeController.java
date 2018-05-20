package com.gire.eval360.controller;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.domain.Employee;
import com.gire.eval360.service.EmployeeService;

@RestController
public class EmployeeController {

	private EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
//	@GetMapping("/employees")
//	@CrossOrigin(origins = "http://localhost:4200")
//	public ResponseEntity<Collection<Employee>> getEmployees(){
//		return ResponseEntity.ok(employeeService.getEmployees());
//	}
}
