package com.gire.eval360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private EmployeeRepository employeeRespository;
	
	@Autowired
	public EmployeeController(EmployeeRepository employeeRespository) {
		this.employeeRespository = employeeRespository;
	}
	
//	@GetMapping("/findByName")
//	@CrossOrigin(origins = "http://localhost:4200")
//	public ResponseEntity<Collection<Employee>> findEmployeesByName(@RequestParam String name){
//		return ResponseEntity.ok(employeeRespository.findEmployeeByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(name, name));
//	}
//	
//	@GetMapping("/findAvailable")
//	@CrossOrigin(origins = "http://localhost:4200")
//	public ResponseEntity<Collection<Employee>> findEmployeesAvailable(@RequestParam String name) {
//		return ResponseEntity.ok(employeeRespository.findEmployeesAvailable(name));
//	}
}
