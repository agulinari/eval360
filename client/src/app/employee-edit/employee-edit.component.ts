import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService } from '../shared/employee.service';
import { GiphyService } from '../shared/giphy.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-employee-edit',
  templateUrl: './employee-edit.component.html',
  styleUrls: ['./employee-edit.component.css']
})
export class EmployeeEditComponent implements OnInit, OnDestroy {

  employee: any = {};
  sub: Subscription;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private employeeService: EmployeeService,
              private giphyService: GiphyService) {

               }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.employeeService.get(id).subscribe((employee: any) => {
          if (employee) {
            this.employee = employee;
            this.employee.href = employee._links.self.href;
            this.giphyService.get(employee.name).subscribe(url => employee.giphyUrl = url);
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

  save(form: NgForm) {
    this.employeeService.save(form).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));
  }

  remove(href) {
    this.employeeService.remove(href).subscribe(result => {
      this.gotoList();
    }, error => console.error(error));
  }
}
