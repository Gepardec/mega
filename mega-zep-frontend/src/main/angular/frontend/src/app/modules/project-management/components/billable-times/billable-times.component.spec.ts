import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

import {BillableTimesComponent} from './billable-times.component';

describe('BillableTimesComponent', () => {
  let component: BillableTimesComponent;
  let fixture: ComponentFixture<BillableTimesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BillableTimesComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillableTimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
