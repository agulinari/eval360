import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';
import { MatDialogConfig, MatDialog, MatDialogRef } from '@angular/material';
import { AddEvalueeDialogComponent } from '../dialog/add-evaluee-dialog.component';
import { Evaluee } from '../domain/project/evaluee';
import { Template } from '../domain/template/template';
import { TemplateService } from '../shared/template.service';
import { debounceTime, tap, switchMap, finalize, timeout } from 'rxjs/operators';
import { User } from '../domain/user/user';
import { ProjectService } from '../shared/project.service';
import { CreateProject } from '../domain/create-project/create-project';
import { CreateEvaluee } from '../domain/create-project/create-evaluee';
import { CreateFeedbackProvider } from '../domain/create-project/create-feedback-provider';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { Router } from '@angular/router';
import { ProjectAdmin } from '../domain/project/project-admin';
import { AddAdminDialogComponent } from '../dialog/add-admin-dialog.component';
import { UserService } from '../shared/user.service';
import { AuthenticationService } from '../shared/authentication.service';
import { CreateAdmin } from '../domain/create-project/create-admin';
import { EvaluationPreviewComponent } from '../evaluation-preview/evaluation-preview.component';
import { CreateReviewer } from '../domain/create-project/create-reviewer';
import { WaitingDialogComponent } from '../dialog/waiting-dialog.component';

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
  adminsFormArray: FormArray;
  selectedTemplate: Template;
  filteredTemplates: Template[] = [];
  isLoading = false;

  constructor(private fb: FormBuilder,
    private dialog: MatDialog,
    private router: Router,
    private templateService: TemplateService,
    private userService: UserService,
    private authService: AuthenticationService,
    private projectService: ProjectService) {}

  ngOnInit() {
    this.selectedTemplate = null;

    this.setAdminCreator();

    this.projectFormGroup = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      adminsFormArray: this.fb.array([], Validators.required)
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


  // --------------- EVALUEES ------------------------------------------


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

  getEvaluees(): number[] {
    const users: number[] = [];
    if (!this.evaluesFormArray) {
      return users;
    }
    this.evaluesFormArray.controls.forEach(c => {
      const user = c.get('evaluee').value.user;
      users.push(user.id);
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

  // --------------- PROJECT ADMINS ----------------------------------------

  setAdminCreator() {
    const userId = this.authService.getUserId();
    this.userService.get(userId).subscribe(user => {
      const admin: ProjectAdmin = {
        id: null,
        user: user,
        creator: true
      };
      this.addAdmin(admin);
    });
  }

  openAddAdminDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = '400px';
    dialogConfig.data = this.getAdmins();
    const dialogRef = this.dialog.open(AddAdminDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data) {
          this.addAdmin(data);
        }
      }
    );
  }

  getAdmins(): number[] {
    const users: number[] = [];
    if (!this.adminsFormArray) {
      return users;
    }
    this.adminsFormArray.controls.forEach(c => {
      const user = c.get('adminInput').value.user;
      users.push(user.id);
    });
    return users;
  }

  addAdmin(admin: ProjectAdmin): void {
    this.adminsFormArray = this.projectFormGroup.get('adminsFormArray') as FormArray;
    const adminForm = this.createAdmin(admin);
    this.adminsFormArray.push(adminForm);
  }

  createAdmin(admin: ProjectAdmin): FormGroup {
    return this.fb.group({
        adminInput: [admin, Validators.required]
     });
  }

  deleteAdmin(index: number): void {
    this.adminsFormArray.removeAt(index);
  }

 // --------------- TEMPLATES --------------------------------------------

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

  // --------------- PROJECT ----------------------------------------------


  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  createProject() {
    
    const dialogRef: MatDialogRef<WaitingDialogComponent> = this.dialog.open(WaitingDialogComponent,  {
      panelClass: 'transparent',
      disableClose: true
    });

     const project = this.prepareSaveProject();
     this.projectService.createProject(project).subscribe(
      res => {
        console.log('Creando proyecto', res);
        dialogRef.close();
      },
      err => {
        console.log('Error creando proyecto', err);
        dialogRef.close();
        this.showError('Se produjo un error al crear el proyecto');
      },
      () => this.gotoList());
  }

  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

  previewTemplate() {
    if (this.selectedTemplate !== undefined) {
      this.dialog.open(EvaluationPreviewComponent, {
        data: {idTemplate: this.selectedTemplate.id}
      });
    }
  }

  prepareSaveProject(): CreateProject {
    const projectModel = this.projectFormGroup.value;
    const evalueesModel = this.evalueeFormGroup.value;
    const templateModel = this.templateFormGroup.value;
    const evalueesArray = this.evalueeFormGroup.get('evaluees') as FormArray;
    const adminsArray = this.projectFormGroup.get('adminsFormArray') as FormArray;

    const saveProject: CreateProject = {
      id: null,
      idTemplate: templateModel.templateInput.id as number,
      name: projectModel.name as string,
      description: projectModel.description as string,
      admins: [],
      evaluees: []
    };

    evalueesArray.controls.forEach( e => {

      const evaluee: CreateEvaluee = {
        id: null,
        idUser: e.value.evaluee.user.id as number,
        feedbackProviders: [],
        reviewers: []
      };

      e.value.evaluee.feedbackProviders.forEach( fp => {

        const feedbackProvider: CreateFeedbackProvider = {
          id: null,
          idUser: fp.user.id as number,
          relationship: fp.relationship as string
        };
        evaluee.feedbackProviders.push(feedbackProvider);
      });

      e.value.evaluee.reviewers.forEach( r => {

        const reviewer: CreateReviewer = {
          id: null,
          idUser: r.user.id as number
        };
        evaluee.reviewers.push(reviewer);
      });

      saveProject.evaluees.push(evaluee);
    });

    adminsArray.controls.forEach( a => {

      const admin: CreateAdmin = {
        id: null,
        idUser: a.value.adminInput.user.id as number,
        creator: a.value.adminInput.creator
      };

      saveProject.admins.push(admin);
    });

    return saveProject;
  }

}
