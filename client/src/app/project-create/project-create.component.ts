import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { AddEvalueeDialogComponent } from '../dialog/add-evaluee-dialog.component';
import { Evaluee } from '../domain/evaluee';
import { Template } from '../domain/template';
import { TemplateService } from '../shared/template.service';
import { debounceTime, tap, switchMap, finalize } from 'rxjs/operators';
import { User } from '../domain/user';
import { ProjectService } from '../shared/project.service';
import { CreateProject } from '../domain/request/create-project';
import { CreateEvaluee } from '../domain/request/create-evaluee';
import { FeedbackProvider } from '../domain/feedback-provider';
import { CreateFeedbackProvider } from '../domain/request/create-feedback-provider';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { Router } from '@angular/router';

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
  selectedTemplate: Template;
  filteredTemplates: Template[] = [];
  isLoading = false;

  constructor(private fb: FormBuilder,
    private dialog: MatDialog,
    private router: Router,
    private templateService: TemplateService,
    private projectService: ProjectService) {}

  ngOnInit() {
    this.selectedTemplate = null;

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
    ).subscribe(tl => this.filteredTemplates = tl.templates);
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

  templateClick(event: any) {
    this.selectedTemplate = event.option.value;
  }

  checkTemplate() {
    if (!this.selectedTemplate || this.selectedTemplate !== this.templateFormGroup.controls['templateInput'].value) {
      this.templateFormGroup.controls['templateInput'].setValue(null);
      this.selectedTemplate = null;
    }
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  createProject() {
     const project = this.prepareSaveProject();
     this.projectService.createProject(project).subscribe(
      res => console.log('Creando proyecto', res),
      err => {
        console.log('Error creando proyecto', err);
        this.showError('Se produjo un error al crear el proyecto');
      },
      () => this.gotoList());
  }

  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

  prepareSaveProject(): CreateProject {
    const projectModel = this.projectFormGroup.value;
    const evalueesModel = this.evalueeFormGroup.value;
    const templateModel = this.templateFormGroup.value;
    const evalueesArray = this.evalueeFormGroup.get('evaluees') as FormArray;

    const saveProject: CreateProject = {
      id: null,
      idTemplate: templateModel.templateInput.id as number,
      name: projectModel.name as string,
      description: projectModel.description as string,
      evaluees: []
    };

    evalueesArray.controls.forEach( e => {

      const evaluee: CreateEvaluee = {
        id: null,
        idUser: e.value.evaluee.user.id as number,
        feedbackProviders: []
      };

      e.value.evaluee.feedbackProviders.forEach( fp => {

        const feedbackProvider: CreateFeedbackProvider = {
          id: null,
          idUser: fp.user.id as number,
          relationship: fp.relationship as string
        };
        evaluee.feedbackProviders.push(feedbackProvider);
      });

      saveProject.evaluees.push(evaluee);
    });

    return saveProject;
  }

}
