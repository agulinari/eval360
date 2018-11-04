import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../shared/user.service';
import { User } from '../domain/user';
import { Subscription } from 'rxjs/Subscription';
import { OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnDestroy, OnChanges {

  user: User;
  userForm: FormGroup;
  sub: Subscription;
  searchTerms = new Subject<string>();

  constructor(private fb: FormBuilder, private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {
    this.createForm();
  }

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

 /* displayFn(employee: Employee): string {
    return employee ? employee.name + ' ' + employee.lastname : '';
  }

  getEmployees() {
    this.employees$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.employeeService.searchAvailables(term))
    );
  }
  */

  getEnabledText() {
    if (this.userForm.controls.enabled.value) {
      return 'Habilitado';
    } else {
      return 'Deshabilitado';
    }
  }

  getUser(id) {
    this.userService.get(id).subscribe((user: User) => {
      if (user) {
        this.user = user;
        this.fillForm();
      } else {
        console.log(`Usuario con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    });
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getUser(id);
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
    this.userForm = this.fb.group({
      username: ['', Validators.compose([Validators.required, Validators.maxLength(30)])],
      password: ['', Validators.compose([Validators.required, Validators.minLength(8), Validators.maxLength(30)])],
      enabled: true
    });
  }

  fillForm() {
    this.userForm.patchValue({
      username: this.user.username,
      password: this.user.password,
      enabled: this.user.enabled
    });
  }

  compare(val1, val2) {
    return val1.id === val2.id;
  }

  rebuildForm() {
  }

  gotoList() {
    this.router.navigate(['/user-list']);
  }

  onSubmit() {
    let id = null;
    if (this.user) {
      id = this.user.id;
    }
    this.user = this.prepareSaveUser(id);
    this.userService.save(this.user).subscribe(/* error handling */);
  }

  prepareSaveUser(id): User {
    const formModel = this.userForm.value;

    const saveUser: User = {
      id: id,
      username: formModel.username as string,
      password: formModel.password as string,
      enabled: formModel.enabled as boolean
    };

    return saveUser;
  }
}
