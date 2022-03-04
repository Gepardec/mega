import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

import {BillableTimesFractionComponent} from './billable-times-fraction.component';

describe('BillableTimesFractionComponent', () => {
  let component: BillableTimesFractionComponent;
  let fixture: ComponentFixture<BillableTimesFractionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BillableTimesFractionComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillableTimesFractionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});
