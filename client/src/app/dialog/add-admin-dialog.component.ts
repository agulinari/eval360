import { Component, OnInit, Inject } from '@angular/core';
import { User } from '../domain/user/user';
import { FormGroup, FormArray, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserService } from '../shared/user.service';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { ProjectAdmin } from '../domain/project/project-admin';

@Component({
    selector: 'app-add-admin-dialog',
    templateUrl: 'add-admin-dialog.component.html',
    styleUrls: ['add-admin-dialog.component.css'],
})
export class AddAdminDialogComponent implements OnInit {

    filteredUsers: User[] = [];
    adminsForm: FormGroup;
    isLoading = false;
    selectedAdmin: User;

    constructor(private dialogRef: MatDialogRef<AddAdminDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private fb: FormBuilder,
        private userService: UserService) {

    }

    ngOnInit() {
        this.selectedAdmin = null;

        this.adminsForm = this.fb.group({
            userInput: ['', Validators.required]
        });

        this.adminsForm.get('userInput')
        .valueChanges
        .pipe(
            debounceTime(300),
            tap(() => this.isLoading = true),
            switchMap(value => this.userService.find(value, 'username,asc', 0, 10).pipe(
                finalize(() => this.isLoading = false)
            ))
        ).subscribe(userList => this.filteredUsers = userList.users.filter(user => !this.data.find(userId => userId === user.id)));
    }

    displayFn(user: User) {
        if (user) {
            return user.username;
        }
    }

    prepareSaveAdmin(): ProjectAdmin {
        const formModel = this.adminsForm.value;

        const saveAdmin: ProjectAdmin = {
          id: null,
          user: formModel.userInput as User,
          creator: false
        };

        return saveAdmin;
      }

    public cancel() {
        this.dialogRef.close(0);
    }

    public ok() {
        const admin = this.prepareSaveAdmin();
        this.dialogRef.close(admin);
    }

    adminClick(event: any) {
        this.selectedAdmin = event.option.value;
    }

    checkAdmin() {
        if (!this.selectedAdmin || this.selectedAdmin !== this.adminsForm.controls['userInput'].value) {
          this.adminsForm.controls['userInput'].setValue(null);
          this.selectedAdmin = null;
        }
    }

}
