import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { TemplateService } from '../shared/template.service';
import { Template } from '../domain/template/template';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { Section } from '../domain/template/section';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Item } from '../domain/template/item';
import { WaitingDialogComponent } from '../dialog/waiting-dialog.component';

@Component({
  selector: 'app-template-edit',
  templateUrl: './template-edit.component.html',
  styleUrls: ['./template-edit.component.css'],
})
export class TemplateEditComponent implements OnInit, OnDestroy {

  templateForm: FormGroup;
  currentTemplate: Template;
  sections: FormArray;
  hasUnitNumber = false;

  private sub: any;
  private subQuery: any;

  sectionTypes = [
    {
      id: 'QUESTIONS',
      value: 'Preguntas'
    },
    {
      id: 'TEXT',
      value: 'Texto'
    }
  ];

  itemTypes = [
    {
      id: 'RATING',
      value: 'Rating'
    },
    {
      id: 'TEXTBOX',
      value: 'Textbox'
    }
  ];

  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private templateService: TemplateService) {

      this.createForm();
    }

  ngOnInit() {
    // Get current template
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.getTemplate(id);
      }
    });

    this.subQuery = this.route.queryParams.subscribe(params => {
      const id = params['clone'];
      if (id) {
        this.cloneTemplate(id);
      }
    });
  }

  createForm() {
    // build form
    this.templateForm = this.fb.group({
      title: [null, Validators.compose([
        Validators.required, Validators.minLength(5), Validators.maxLength(25)])
      ],
      sections: this.fb.array([], Validators.required)
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.subQuery.unsubscribe();
  }

  loadTemplateForm(template: Template): void {
    this.templateForm.patchValue({
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

  cloneTemplate(id) {
    this.templateService.get(id).subscribe((template: Template) => {
      if (template) {
        this.currentTemplate = template;
        this.currentTemplate.id = null;
        this.currentTemplate.title = null;
        this.loadTemplateForm(this.currentTemplate);
      } else {
        console.log(`Template con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    });
  }

  getTemplate(id) {
    this.templateService.get(id).subscribe((template: Template) => {
      if (template) {
        this.currentTemplate = template;
        this.loadTemplateForm(template);
      } else {
        console.log(`Template con id '${id}' no encontrado, volviendo a la lista`);
        this.gotoList();
      }
    });
  }

  gotoList() {
    this.router.navigate(['/main/template-list']);
  }

  createSection(): FormGroup {
    return this.fb.group({
      name: [null, Validators.required],
      description: [null, Validators.required],
      sectionType: ['QUESTIONS', Validators.required],
      items: this.fb.array([], Validators.required)
    });
  }

  createItem(): FormGroup {
    return this.fb.group({
      title: [null, Validators.required],
      description: [null, Validators.required],
      itemType: [null, Validators.required]
    });
  }

  addSection(): FormGroup {
    this.sections = this.templateForm.get('sections') as FormArray;
    const section = this.createSection();
    this.sections.push(section);
    return section;
  }

  addItem(control: FormArray): FormGroup {
    const item = this.createItem();
    control.push(item);
    return item;
  }

  deleteSection(index: number): void {
    this.sections.removeAt(index);
  }

  deleteItem(control: FormArray, index: number): void {
    control.removeAt(index);
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  onSubmit() {

    const dialogRef: MatDialogRef<WaitingDialogComponent> = this.dialog.open(WaitingDialogComponent,  {
      panelClass: 'transparent',
      disableClose: true
    });

    let id = null;
    if (this.currentTemplate) {
      id = this.currentTemplate.id;
    }
    this.currentTemplate = this.prepareSaveTemplate(id);
    this.templateService.save(this.currentTemplate).subscribe(
      res => {
        console.log('Guardando template', res);
        dialogRef.close();
      },
      err => {
        console.log('Error guardando template', err);
        dialogRef.close();
        if (err.status === 409) {
          this.showError('El template ya existe');
        } else {
          this.showError('Se produjo un error al guardar el template');
        }
      },
      () => this.gotoList());
  }

  prepareSaveTemplate(id): Template {
    const formModel = this.templateForm.value;
    const sections = this.templateForm.get('sections') as FormArray;

    const saveTemplate: Template = {
      id: id,
      title: formModel.title as string,
      sections: []
    };

    sections.controls.forEach( s => {
      const items = s.get('items') as FormArray;
      const section: Section = {
        id: s.value.id,
        name: s.value.name,
        description: s.value.description,
        sectionType: s.value.sectionType,
        position: s.value.position,
        items: []
      };

      items.controls.forEach( i => {
        const item: Item = {
          id: i.value.id,
          title: i.value.title,
          description: i.value.description,
          itemType: i.value.itemType,
          position: i.value.position
        };
        section.items.push(item);
      });

      saveTemplate.sections.push(section);
    });

    return saveTemplate;
  }
}
