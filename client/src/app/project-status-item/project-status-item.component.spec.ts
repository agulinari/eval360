import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectStatusItemComponent } from './project-status-item.component';

describe('ProjectStatusItemComponent', () => {
  let component: ProjectStatusItemComponent;
  let fixture: ComponentFixture<ProjectStatusItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectStatusItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectStatusItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
