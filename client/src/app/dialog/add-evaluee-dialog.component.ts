import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { UserService } from '../shared/user.service';
import { User } from '../domain/user';
import { Evaluee } from '../domain/evaluee';
import { FeedbackProvider } from '../domain/feedback-provider';


@Component({
    selector: 'app-add-evaluee-dialog',
    templateUrl: 'add-evaluee-dialog.component.html',
    styleUrls: ['add-evaluee-dialog.component.css'],
})
export class AddEvalueeDialogComponent implements OnInit {

    filteredUsers: User[] = [];
    filteredFps: User[] = [];
    evalueesForm: FormGroup;
    isLoading = false;
    feedbackProviders: FormArray;
    selectedEvaluee: User;
    selectedFPs: User[];


    constructor(private dialogRef: MatDialogRef<AddEvalueeDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private fb: FormBuilder,
        private userService: UserService) {

    }

    ngOnInit() {
        this.selectedEvaluee = null;
        this.selectedFPs = [];

        this.evalueesForm = this.fb.group({
            userInput: ['', Validators.required],
            feedbackProviders: this.fb.array([], Validators.required)
        });

        this.evalueesForm.get('userInput')
        .valueChanges
        .pipe(
            debounceTime(300),
            tap(() => this.isLoading = true),
            switchMap(value => this.userService.find(value, 'username,asc', 0, 10).pipe(
                finalize(() => this.isLoading = false)
            ))
        ).subscribe(userList => this.filteredUsers = userList.users.filter(user => !this.data.find(u => u.id === user.id)));
    }

    displayFn(user: User) {
        if (user) {
            return user.username;
        }
    }

    addFeedbackProvider() {
        this.feedbackProviders = this.evalueesForm.get('feedbackProviders') as FormArray;
        const feedbackProvider = this.createFeedbackProvider();
        this.feedbackProviders.push(feedbackProvider);
        return feedbackProvider;
    }

    createFeedbackProvider(): FormGroup {
        const feedbackProvider = this.fb.group({
            fpInput: ['', Validators.required],
            relationship: ['', Validators.required]
        });
        feedbackProvider.get('fpInput')
        .valueChanges
        .pipe(
            debounceTime(300),
            tap(() => this.isLoading = true),
            switchMap(value => this.userService.find(value, 'username,asc', 0, 10).pipe(
                finalize(() => this.isLoading = false)
            ))
        ).subscribe(userList => {
            this.filteredFps = userList.users.filter(user => !this.selectedFPs.find(u => u.id === user.id));
        });
        return feedbackProvider;
    }

    deleteFeedbackProvider(index: number): void {
        this.selectedFPs.splice(index, 1);
        this.feedbackProviders.removeAt(index);
    }

    prepareSaveEvaluee(): Evaluee {
        const formModel = this.evalueesForm.value;
        const feedbackProviders = this.evalueesForm.get('feedbackProviders') as FormArray;

        const saveEvaluee: Evaluee = {
          id: null,
          user: formModel.userInput as User,
          feedbackProviders: []
        };

        feedbackProviders.controls.forEach( fp => {

          const feedbackProvider: FeedbackProvider = {
            id: null,
            user: fp.value.fpInput as User,
            relationship: fp.value.relationship
          };

          saveEvaluee.feedbackProviders.push(feedbackProvider);
        });

        return saveEvaluee;
      }

    public cancel() {
        this.dialogRef.close(0);
    }

    public ok() {
        const evaluee = this.prepareSaveEvaluee();
        this.dialogRef.close(evaluee);
    }

    evalueeClick(event: any) {
        this.selectedEvaluee = event.option.value;
    }

    checkEvaluee() {
        if (!this.selectedEvaluee || this.selectedEvaluee !== this.evalueesForm.controls['userInput'].value) {
          this.evalueesForm.controls['userInput'].setValue(null);
          this.selectedEvaluee = null;
        }
    }

    fpClick(event: any, index: number) {
        this.selectedFPs[index] = event.option.value;
    }

    checkFp(index: number) {
        if (!this.selectedFPs[index] || this.selectedFPs[index] !== this.feedbackProviders.controls[index].get('fpInput').value) {
            this.feedbackProviders.controls[index].get('fpInput').setValue(null);
            this.selectedFPs.splice(index, 1);
        }
    }
}
