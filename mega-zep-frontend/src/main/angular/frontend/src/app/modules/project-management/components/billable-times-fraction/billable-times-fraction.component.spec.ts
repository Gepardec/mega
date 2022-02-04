import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

import {BillableTimesFractionComponent} from './billable-times-fraction.component';

describe('BillableTimesFractionComponent', () => {
  let component: BillableTimesFractionComponent;
  let fixture: ComponentFixture<BillableTimesFractionComponent>;

  beforeEach(waitForAsync(() => {
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  // TODO test billable and non-billable times are '00:00'

  // TODO test billable and non-billable times are != '00:00'
});
