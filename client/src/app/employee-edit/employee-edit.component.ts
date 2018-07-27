import { Component, OnInit, Input } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeService } from '../shared/employee.service';
import { AreaService } from '../shared/area.service';
import { Employee } from '../domain/employee';
import { Area } from '../domain/area';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OnChanges, OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { PositionService } from '../shared/position.service';

@Component({
  selector: 'app-employee-edit',
  templateUrl: './employee-edit.component.html',
  styleUrls: ['./employee-edit.component.css']
})
export class EmployeeEditComponent implements OnInit, OnChanges, OnDestroy {

  employee: Employee;
  employeeForm: FormGroup;
  sub: Subscription;
  areas: Area[];
  positions: Position[];
  private roles: String[];

  constructor(private fb: FormBuilder, private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private areaService: AreaService,
    private positionService: PositionService) {
    this.createForm();
  }

  getAreas() {
    this.areaService.getAll()
      .subscribe(data => {
        this.areas = data;
      });
  }

  getPositions() {
    this.positionService.getAll()
      .subscribe(data => {
        this.positions = data;
      });
  }

  getEmployee(id) {
    this.employeeService.get(id).subscribe((employee: Employee) => {
      if (employee) {
        this.employee = employee;
        this.fillForm();
      } else {
        console.log(`Empleado con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    });
  }

  ngOnInit() {
    this.getAreas();
    this.getPositions();
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getEmployee(id);
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  ngOnChanges() {
    this.rebuildForm();
  }

  createForm() {
    this.employeeForm = this.fb.group({
      name: ['', Validators.compose([Validators.required, Validators.maxLength(30)])],
      lastname: ['', Validators.compose([Validators.required, Validators.maxLength(30)])],
      birthDate: ['', Validators.required],
      startDate: ['', Validators.required],
      photo: '',
      contactInfo: this.fb.group({
        internalNumber: '',
        telephoneNumber: '',
        email: ['', Validators.compose([Validators.required, Validators.email])],
      }),
      address: this.fb.group({
        address: '',
        city: '',
        state: '',
        postalCode: ''
      }),
      area: '',
      position: ''
    });
  }

  fillForm() {
    this.employeeForm.patchValue({
      name: this.employee.name,
      lastname: this.employee.lastname,
      birthDate: this.employee.birthDate,
      startDate: this.employee.startDate,
      photo: this.employee.photo,
      contactInfo: {
        internalNumber: this.employee.contactInfo.internalNumber,
        telephoneNumber: this.employee.contactInfo.telephoneNumber,
        email: this.employee.contactInfo.email
      },
      address: {
        address: this.employee.address.address,
        city: this.employee.address.city,
        state: this.employee.address.state,
        postalCode: this.employee.address.postalCode
      },
      area: this.employee.area,
      position: this.employee.position
    });
  }

  compare(val1, val2) {
    return val1.id === val2.id;
  }

  rebuildForm() {
  }

  gotoList() {
    this.router.navigate(['/employee-list']);
  }

  onSubmit() {
    let id = null;
    if (this.employee) {
      id = this.employee.id;
    }
    this.employee = this.prepareSaveEmployee(id);
    this.employeeService.save(this.employee).subscribe(/* error handling */);
  }

  prepareSaveEmployee(id): Employee {
    const formModel = this.employeeForm.value;

    const saveEmployee: Employee = {
      id: id,
      name: formModel.name as string,
      lastname: formModel.lastname as string,
      birthDate: formModel.birthDate as Date,
      startDate: formModel.startDate as Date,
      photo: formModel.photo as string,
      contactInfo: {
        id: id,
        internalNumber: formModel.contactInfo.internalNumber as string,
        telephoneNumber: formModel.contactInfo.telephoneNumber as string,
        email: formModel.contactInfo.email as string
      },
      address: {
        id: id,
        address: formModel.address.address as string,
        city: formModel.address.city as string,
        state: formModel.address.state as string,
        postalCode: formModel.address.postalCode as string
      },
      area: formModel.area,
      position: formModel.position
    };

    return saveEmployee;
  }
}
