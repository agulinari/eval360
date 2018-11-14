import { Component, OnInit, OnDestroy, ViewChild, ElementRef, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../shared/user.service';
import { User } from '../domain/user';
import { Subscription ,  Subject ,  Observable } from 'rxjs';
import { OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { map, startWith } from 'rxjs/operators';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent, MatDialog } from '@angular/material';
import { Authority } from '../domain/authority';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnDestroy, OnChanges {

  user: User;
  authorities: FormArray;
  userForm: FormGroup;
  sub: Subscription;
  searchTerms = new Subject<string>();

  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = false;
  separatorKeyCodes: number[] = [ENTER, COMMA];
  roleCtrl = new FormControl();
  filteredRoles: Observable<Authority[]>;
  roles: Authority[] = [];
  allRoles: Authority[] = [{ id: 1, name: 'ROLE_USER'}, { id: 2, name: 'ROLE_ADMIN'}];

  @ViewChild('roleInput') roleInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(private fb: FormBuilder, 
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private userService: UserService) {
    this.createForm();
    this.filteredRoles = this.roleCtrl.valueChanges.pipe(
      startWith(null),
      map((role: any | null) => {
        if (!role) {
          return this.allRoles.slice();
        }
        if (role.name) {
          return this.allRoles.slice();
        } else {
          return this._filter(role);
        }
      })
    );
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  remove(index: number): void {
    const authorities = this.userForm.get('authorities') as FormArray;
    if (index >= 0) {
      authorities.removeAt(index);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const role = event.option.value;
    const authorities = this.userForm.get('authorities') as FormArray;

    let add = true;
    authorities.controls.forEach( (a) => {
      if (a.value.id === role.id) {
        add = false;
      }
    });

    if (add) {
      authorities.push(this.fb.control(role));
      this.roleInput.nativeElement.value = '';
      this.roleCtrl.setValue(null);
   }

  }

  private _filter(value: string): Authority[] {
    const filterValue = value.toLowerCase();
    return this.allRoles.filter(role => role.name.toLowerCase().indexOf(filterValue) === 0);
  }

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
      enabled: true,
      authorities: this.fb.array([])
    });
  }

  createAuthority(): FormGroup {
    return this.fb.group({
      id: [null],
      name: [null, Validators.required]
    });
  }

  fillForm() {
    this.userForm.patchValue({
      username: this.user.username,
      password: this.user.password,
      enabled: this.user.enabled
    });
    this.user.authorities.forEach( a => {
      const authority = this.addAuthority();
      authority.patchValue({
        id: a.id,
        name: a.name
      });
    });
  }

  addAuthority(): FormGroup {
    this.authorities = this.userForm.get('authorities') as FormArray;
    const authority = this.createAuthority();
    this.authorities.push(authority);
    return authority;
  }

  compare(val1, val2) {
    return val1.id === val2.id;
  }

  rebuildForm() {
  }

  gotoList() {
    this.router.navigate(['/main/user-list']);
  }

  onSubmit() {
    let id = null;
    if (this.user) {
      id = this.user.id;
    }
    this.user = this.prepareSaveUser(id);
    this.userService.save(this.user).subscribe(
      res => console.log('Guardando usuario', res),
      err => {
        console.log('Error guardando usuario', err);
        if (err.status === 409) {
          this.showError('El usuario ya existe');
        } else {
          this.showError('Se produjo un error al guardar el usuario');
        }
      },
      () => this.gotoList());
  }

  prepareSaveUser(id): User {
    const formModel = this.userForm.value;
    const authorities = this.userForm.get('authorities') as FormArray;

    const saveUser: User = {
      id: id,
      username: formModel.username as string,
      password: formModel.password as string,
      enabled: formModel.enabled as boolean,
      authorities: []
    };

    authorities.controls.forEach( c => {
      const role: Authority = {
        id: c.value.id,
        name: c.value.name
      };
      saveUser.authorities.push(role);
    });

    return saveUser;
  }
}
