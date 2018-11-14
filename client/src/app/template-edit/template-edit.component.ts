import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray } from '@angular/forms';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { TemplateService } from '../shared/template.service';
import { Template } from '../domain/template';
import { map, catchError, switchMap } from 'rxjs/operators';
import { IterableChangeRecord_ } from '@angular/core/src/change_detection/differs/default_iterable_differ';

@Component({
  selector: 'template-edit',
  templateUrl: './template-edit.component.html',
  styleUrls: ['./template-edit.component.css'],
})
export class TemplateEditComponent implements OnInit, OnDestroy {

  templateForm: FormGroup;
  currentTemplate: Template;
  sections: FormArray;
  hasUnitNumber = false;

  private sub: any;

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
      id: 'COMBO',
      value: 'Combo'
    },
    {
      id: 'CHECKBOX',
      value: 'Checkbox'
    },
    {
      id: 'OPTIONS',
      value: 'Options'
    },
    {
      id: 'TEXTBOX',
      value: 'Textbox'
    }
  ];

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router, private templateService: TemplateService) {}

  ngOnInit() {

    // 1. build form
    this.templateForm = this.fb.group({
      title: [null, Validators.compose([
        Validators.required, Validators.minLength(5), Validators.maxLength(25)])
      ],
      sections: this.fb.array([])
    });

    // 2. get current template
    this.sub = this.route.params.pipe(
       switchMap((params: Params) => this.templateService.get(params['id'])))
      .subscribe((template: Template) => {
        // 3. fill in form fields with values from retrieved template
        this.currentTemplate = template;
        this.loadTemplateForm(template);
      });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  loadTemplateForm(template: Template): void {
    this.templateForm.patchValue({
      title: template.title
    });
    template.sections.forEach( s => {
      const section = this.addSection();
      section.patchValue({
        name: s.name,
        description: s.description,
        type: s.sectionType
      });

      s.items.forEach( i => {
        const item = this.addItem(section.get('items') as FormArray);
        item.patchValue({
          title: i.title,
          description: i.description,
          type: i.itemType
        });
      });
    });
  }

  createSection(): FormGroup {
    return this.fb.group({
      name: [null, Validators.required],
      description: [null, Validators.required],
      type: [null, Validators.required],
      items: this.fb.array([])
    });
  }

  createItem(): FormGroup {
    return this.fb.group({
      title: [null, Validators.required],
      description: [null, Validators.required],
      type: [null, Validators.required]
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

  onSubmit() {
    alert('Thanks!');
  }
}
