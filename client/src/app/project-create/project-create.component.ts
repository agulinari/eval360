import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { AddEvalueeDialogComponent } from '../dialog/add-evaluee-dialog.component';
import { Evaluee } from '../domain/evaluee';
import { Template } from '../domain/template';
import { TemplateService } from '../shared/template.service';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { User } from '../domain/user';

/**
 * @title Stepper overview
 */
@Component({
  selector: 'app-project-create',
  templateUrl: 'project-create.component.html',
  styleUrls: ['project-create.component.css'],
})
export class ProjectCreateComponent implements OnInit {
  projectFormGroup: FormGroup;
  evalueeFormGroup: FormGroup;
  templateFormGroup: FormGroup;
  evaluesFormArray: FormArray;
  filteredTemplates: Template[] = [];
  isLoading = false;

  constructor(private fb: FormBuilder,
    private dialog: MatDialog,
    private templateService: TemplateService) {}

  ngOnInit() {
    this.projectFormGroup = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
    this.evalueeFormGroup = this.fb.group({
      evaluees: this.fb.array([], Validators.required)
    });
    this.templateFormGroup = this.fb.group({
      templateInput: ['', Validators.required]
    });

    this.templateFormGroup.get('templateInput')
    .valueChanges
    .pipe(
        debounceTime(300),
        tap(() => this.isLoading = true),
        switchMap(value => this.templateService.find(value, 'username,asc', 0, 10).pipe(
            finalize(() => this.isLoading = false)
        ))
    ).subscribe(templates => this.filteredTemplates = templates);
  }

  openAddEvalueeDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = '400px';
    dialogConfig.data = this.getEvaluees();
    const dialogRef = this.dialog.open(AddEvalueeDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data) {
          this.addEvaluee(data);
        }
      }
    );
  }

  getEvaluees(): User[] {
    const users: User[] = [];
    if (!this.evaluesFormArray) {
      return users;
    }
    this.evaluesFormArray.controls.forEach(c => {
      const user = c.get('evaluee').value.user;
      users.push(user);
    });
    return users;
  }

  addEvaluee(evaluee: Evaluee): void {
    this.evaluesFormArray = this.evalueeFormGroup.get('evaluees') as FormArray;
    const evalueeForm = this.createEvaluee(evaluee);
    this.evaluesFormArray.push(evalueeForm);
  }

  createEvaluee(evaluee: Evaluee): FormGroup {
    return this.fb.group({
      evaluee: [evaluee, Validators.required]
    });
  }

  deleteEvaluee(index: number): void {
    this.evaluesFormArray.removeAt(index);
  }

  displayTemplateFn(template: Template) {
    if (template) {
        return template.title;
    }
  }

}
