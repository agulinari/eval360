import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsActiveProjectsComponent } from './stats-active-projects.component';

describe('StatsActiveProjectsComponent', () => {
  let component: StatsActiveProjectsComponent;
  let fixture: ComponentFixture<StatsActiveProjectsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsActiveProjectsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsActiveProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
