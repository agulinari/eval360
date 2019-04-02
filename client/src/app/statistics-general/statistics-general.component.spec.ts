import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsGeneralComponent } from './statistics-general.component';

describe('StatisticsGeneralComponent', () => {
  let component: StatisticsGeneralComponent;
  let fixture: ComponentFixture<StatisticsGeneralComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatisticsGeneralComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatisticsGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
