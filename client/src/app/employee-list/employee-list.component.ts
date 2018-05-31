import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../shared/employee.service';
import { GiphyService } from '../shared/giphy.service';
import { Employee } from '../domain/employee';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {

  employees: Array<Employee>;

  constructor(private employeeService: EmployeeService, private giphyService: GiphyService) { }

  ngOnInit() {
    this.employeeService.getAll().subscribe(data => {
      this.employees = data._embedded.employees;
    });
  }

}
