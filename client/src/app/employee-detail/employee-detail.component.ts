import { Component, OnInit } from '@angular/core';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService } from '../shared/employee.service';
import { Employee } from '../domain/employee';
import { AuthenticationService } from '../shared/authentication.service';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css']
})
export class EmployeeDetailComponent implements OnInit, OnDestroy {

  private roles: String[];
  employee: Employee;
  sub: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private employeeService: EmployeeService,
              private authenticationService: AuthenticationService) {

               }

  ngOnInit() {
    this.roles = this.authenticationService.getRoles();
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.employeeService.get(id).subscribe((employee: Employee) => {
          if (employee) {
            this.employee = employee;
          } else {
            console.log(`Empleado con id '${id}' no encontrado, volviendo a la lista`);
            this.gotoList();
          }
        });
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  gotoList() {
    this.router.navigate(['/employee-list']);
  }

  edit(id: string) {
    this.router.navigate(['/employee-edit', id]);
  }

  remove(id: string) {
    this.employeeService.remove(id).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));
  }

}
