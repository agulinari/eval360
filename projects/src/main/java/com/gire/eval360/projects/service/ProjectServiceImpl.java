package com.gire.eval360.projects.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.gire.eval360.projects.domain.Employee;
import com.gire.eval360.projects.repository.EmployeeRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	private EmployeeRepository employeeRepository;
	
	public ProjectServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public Collection<Employee> getEmployees() {
		return employeeRepository.findAll();
	}
}
