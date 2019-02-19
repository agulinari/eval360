import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TemplateService } from '../shared/template.service';
import { Template } from '../domain/template/template';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';


@Component({
  selector: 'app-evaluation-preview',
  templateUrl: './evaluation-preview.component.html',
  styleUrls: ['./evaluation-preview.component.css']
})
export class EvaluationPreviewComponent implements OnInit {

  evaluationForm: FormGroup;
  template: Template;
  sections: FormArray;
  loading = false;

  constructor(private dialogRef: MatDialogRef<EvaluationPreviewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialog: MatDialog,
    private fb: FormBuilder,
    private templateService: TemplateService) {
      this.createForm();
  }

  ngOnInit() {
    // Get current template
    this.getTemplate(this.data.idTemplate);
  }


  createForm() {
    // build form
    this.evaluationForm = this.fb.group({
      title: null,
      sections: this.fb.array([], Validators.required)
    });
  }

  getTemplate(id) {
    this.templateService.get(id).subscribe((template: Template) => {
      if (template) {
        this.template = template;
        this.loadEvaluationForm(template);
      } else {
        console.log(`Template con id '${id}' no encontrado, volviendo a la lista`);
       // this.gotoList();
      }
    },
    err => {
      console.log('Error obteniendo template', err);
      this.showError('Se produjo un error al  obtener el template');
      this.close();
    },
    () => {
      this.loading = false;
    });
  }

  public close() {
    this.dialogRef.close(0);
  }

  loadEvaluationForm(template: Template): void {

    this.evaluationForm.patchValue({
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
      name: null,
      description: null,
      sectionType: null,
      items: this.fb.array([], Validators.required)
    });
  }

  createItem(): FormGroup {
    return this.fb.group({
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

}


