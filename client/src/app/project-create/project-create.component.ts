import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormArray} from '@angular/forms';

/**
 * @title Stepper overview
 */
@Component({
  selector: 'app-project-create',
  templateUrl: 'project-create.component.html',
  styleUrls: ['project-create.component.css'],
})
export class ProjectCreateComponent implements OnInit {
  evalueeFormGroup: FormGroup;
  templateFormGroup: FormGroup;
  evaluees: FormArray;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.evalueeFormGroup = this.fb.group({
      evaluees: this.fb.array([], Validators.required)
    });
    this.templateFormGroup = this.fb.group({
      template: ['', Validators.required]
    });
  }

  createEvaluee(): FormGroup {
    return this.fb.group({
      idUser: [null, Validators.required],
      username: [null, Validators.required],
      mail: [null, Validators.required],
      feedbackProviders: this.fb.array([], Validators.required)
    });
  }

  createFeedbackProvider(): FormGroup {
    return this.fb.group({
      idUser: [null, Validators.required],
      username: [null, Validators.required],
      mail: [null, Validators.required]
    });
  }

  addEvaluee(): FormGroup {
    this.evaluees = this.evalueeFormGroup.get('evaluees') as FormArray;
    const evaluee = this.createEvaluee();
    this.evaluees.push(evaluee);
    return evaluee;
  }

  addFeedbackProvider(control: FormArray): FormGroup {
    const feedbackProvider = this.createFeedbackProvider();
    control.push(feedbackProvider);
    return feedbackProvider;
  }

  deleteEvaluee(index: number): void {
    this.evaluees.removeAt(index);
  }

  deleteFeedbackProvider(control: FormArray, index: number): void {
    control.removeAt(index);
  }
}
