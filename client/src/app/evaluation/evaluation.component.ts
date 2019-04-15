import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';
import { TemplateService } from '../shared/template.service';
import { EvaluationService } from '../shared/evaluation.service';
import { Template } from '../domain/template/template';
import { Subscription } from 'rxjs';
import { ProjectService } from '../shared/project.service';
import { AuthenticationService } from '../shared/authentication.service';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { Project } from '../domain/project/project';
import { Evaluation } from '../domain/evaluation/evaluation';
import { Section } from '../domain/evaluation/section';
import { Item } from '../domain/evaluation/item';
import { UserService } from '../shared/user.service';
import { User } from '../domain/user/user';
import { WaitingDialogComponent } from '../dialog/waiting-dialog.component';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss']
})
export class EvaluationComponent implements OnInit, OnDestroy {

  sub: Subscription;
  evaluationForm: FormGroup;
  project: Project;
  template: Template;
  idEvalueeFeedbackProvider: number;
  sections: FormArray;
  loading = false;

  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private templateService: TemplateService,
    private projectService: ProjectService,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private evaluationService: EvaluationService) {

    this.createForm();
  }

  ngOnInit() {
    // Get current template
    this.sub = this.route.params.subscribe(params => {
      const idProject = params['idProject'];
      this.idEvalueeFeedbackProvider = params['idEvaluee'];
      if (idProject) {
        this.getProject(idProject);
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  createForm() {
    // build form
    this.evaluationForm = this.fb.group({
      title: null,
      username: null,
      relationship: null,
      sections: this.fb.array([], Validators.required)
    });
  }

  getUserEvaluee(idUser, relationship, idTemplate) {
    this.userService.get(idUser).subscribe((user: User) => {
      if (user) {
        this.getTemplate(user, relationship, idTemplate);
      } else {
        console.log(`Usuario con id '${idUser}' no encontrado, volviendo a la lista`);
        this.gotoProjectList();
      }
    },
    err => {
      console.log('Error obteniendo usuario', err);
      this.showError('Se produjo un error al  obtener el usuario');
      this.gotoProjectList();
    },
    () => {
      this.loading = false;
    });
  }

  getTemplate(user, relationship, id) {
    this.templateService.get(id).subscribe((template: Template) => {
      if (template) {
        this.template = template;
        this.loadEvaluationForm(user, relationship, template);
      } else {
        console.log(`Template con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoProjectList();
      }
    },
    err => {
      console.log('Error obteniendo template', err);
      this.showError('Se produjo un error al  obtener el template');
      this.gotoProjectList();
    },
    () => {
      this.loading = false;
    });
  }

  getProject(id) {
    this.loading = true;
    this.projectService.get(id).subscribe((project: Project) => {

      if (project) {

        // Chequear que el usuario sea el evaluador
        const userId = this.authenticationService.getUserId();
        let fp;
        project.evaluees.forEach(evaluee => {
          const evalueFp = evaluee.feedbackProviders.find(efp => efp.id === +this.idEvalueeFeedbackProvider);
          if (evalueFp !== undefined) {
            fp = evalueFp.feedbackProvider;
          }
        });

        if ((fp === undefined) || (fp.idUser !== +userId)) {
          console.log('El usuario no es dueño de la evaluacion, volviendo a la lista');
          this.gotoProjectList();
        }

        // Busco el evaluado
        project.evaluees.forEach(evaluee => {
          const evalueFp = evaluee.feedbackProviders.find(efp => efp.id === +this.idEvalueeFeedbackProvider);
          if (evalueFp !== undefined) {
            this.getUserEvaluee(evaluee.idUser, evalueFp.relationship, project.idEvaluationTemplate);
            this.project = project;
          }
        });

       } else {
        console.log(`Proyecto con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoProjectList();
      }
    },
    err => {
      console.log('Error obteniendo proyecto', err);
      this.showError('Se produjo un error al  obtener al proyecto');
      this.gotoProjectList();
    },
    () => {
      this.loading = false;
    });
  }

  loadEvaluationForm(user: User, relationship: string, template: Template): void {

    this.evaluationForm.patchValue({
      username: user.username,
      relationship: relationship,
      title: template.title
    });

    template.sections.forEach( s => {
      const section = this.addSection();
      section.patchValue({
        id: s.id,
        name: s.name,
        description: s.description,
        sectionType: s.sectionType
      });

      s.items.forEach( i => {
        const item = this.addItem(section.get('items') as FormArray);
        item.patchValue({
          id: i.id,
          title: i.title,
          description: i.description,
          itemType: i.itemType
        });
      });
    });
  }

  addSection(): FormGroup {
    this.sections = this.evaluationForm.get('sections') as FormArray;
    const section = this.createSection();
    this.sections.push(section);
    return section;
  }

  addItem(control: FormArray): FormGroup {
    const item = this.createItem();
    control.push(item);
    return item;
  }

  createSection(): FormGroup {
    return this.fb.group({
      id: null,
      name: null,
      description: null,
      sectionType: null,
      items: this.fb.array([], Validators.required)
    });
  }

  createItem(): FormGroup {
    return this.fb.group({
      id: null,
      title: null,
      description: null,
      itemType: null,
      value: [null, Validators.required],
      value1: null
    });
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  gotoProjectList() {
    this.router.navigate(['/main/project-list']);
  }

  gotoEvaluationList() {
    this.router.navigate([`/main/project-tasks/${this.project.id}`]);
  }

  onSubmit() {
    const dialogRef: MatDialogRef<WaitingDialogComponent> = this.dialog.open(WaitingDialogComponent,  {
      panelClass: 'transparent',
      disableClose: true
    });
    const evaluation = this.prepareSaveEvaluation();
    this.evaluationService.save(evaluation).subscribe(
      res => {
        console.log('Guardando evaluacion', res);
        dialogRef.close();
      },
      err => {
        console.log('Error guardando evaluacion', err);
        dialogRef.close();
        this.showError('Se produjo un error al guardar la evaluacion');
      },
      () => this.gotoEvaluationList());
  }

  getEvaluee(): number {
    const evaluee = this.project.evaluees
    .find(e => e.feedbackProviders.find(efp => efp.id === +this.idEvalueeFeedbackProvider) !== undefined);
    return evaluee.id;
  }

  getFeedbackProvider(): number {
    const userId = this.authenticationService.getUserId();
    const feedbackProvider = this.project.feedbackProviders.find(fp => fp.idUser === +userId);
    return feedbackProvider.id;
  }

  prepareSaveEvaluation(): Evaluation {
    const formModel = this.evaluationForm.value;
    const sections = this.evaluationForm.get('sections') as FormArray;

    const saveEvaluation: Evaluation = {
      id: null,
      idProject: this.project.id,
      idEvaluee: this.getEvaluee(),
      idFeedbackProvider: this.getFeedbackProvider(),
      idTemplate: this.template.id,
      username: formModel.username,
      relationship: formModel.relationship,
      sections: []
    };

    sections.controls.forEach( s => {
      const items = s.get('items') as FormArray;
      const section: Section = {
        id: s.value.id,
        items: []
      };

      items.controls.forEach( i => {
        const item: Item = {
          id: i.value.id,
          type: i.value.itemType,
          value: i.value.value,
          value1: i.value.value1
        };
        section.items.push(item);
      });

      saveEvaluation.sections.push(section);
    });

    return saveEvaluation;
  }

}


