package com.gire.eval360.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.gire.eval360.domain.Employee;
import com.gire.eval360.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private EmployeeRepository employeeRepository;
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Collection<Employee> getEmployees() {
		return employeeRepository.findAll();
	}
}
