import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { TemplateService } from '../shared/template.service';
import { EvaluationService } from '../shared/evaluation.service';
import { Template } from '../domain/template/template';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.css']
})
export class EvaluationComponent implements OnInit, OnDestroy {

  sub: Subscription;
  evaluationForm: FormGroup;
  template: Template;
  sections: FormArray;

  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private templateService: TemplateService,
    private evaluationService: EvaluationService) {

    this.createForm();
  }

  ngOnInit() {
    // Get current template
    this.sub = this.route.params.subscribe(params => {
      const id = params['idTemplate'];
      if (id) {
        this.getTemplate(id);
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
    });
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
      type: null,
      content: null,
      value: [null, Validators.required]
    });
  }

}
