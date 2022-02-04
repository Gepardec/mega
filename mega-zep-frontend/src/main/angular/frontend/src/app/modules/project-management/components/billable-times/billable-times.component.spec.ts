import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

import {BillableTimesComponent} from './billable-times.component';
import {By} from '@angular/platform-browser';
import {expect} from "@angular/flex-layout/_private-utils/testing";

describe('BillableTimesComponent', () => {
  let component: BillableTimesComponent;
  let fixture: ComponentFixture<BillableTimesComponent>;

  beforeEach(waitForAsync(() => {
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display (non-)billable and transform them to fraction number', () => {
    component.billableTimes = '80:30';
    component.nonBillableTimes = '04:15';

    fixture.detectChanges();

    expect(billableTimesValue()).toEqual('80,50');
    expect(nonBillableTimesValue()).toEqual('4,25');
  });

  it('should display horizontal rule if (non-)billable times are "00:00"', () => {
    component.billableTimes = '00:00';
    component.nonBillableTimes = '00:00';

    fixture.detectChanges();

    const billableTimesMatIcon = billableTimesElement().children[0];
    expect(billableTimesMatIcon.name).toEqual("mat-icon");
    expect(billableTimesMatIcon.nativeElement.innerHTML).toEqual("horizontal_rule");

    const nonBillableTimesMatIcon = nonBillableTimesElement().children[0];
    expect(nonBillableTimesMatIcon.name).toEqual("mat-icon");
    expect(nonBillableTimesMatIcon.nativeElement.innerHTML).toEqual("horizontal_rule");
  });


  function billableTimesValue() {
    return billableTimesElement().nativeElement.innerHTML;
  }

  function nonBillableTimesValue() {
    return nonBillableTimesElement().nativeElement.innerHTML;
  }

  function billableTimesElement() {
    return fixture.debugElement.query(By.css('.times-col-left'));
  }
  function nonBillableTimesElement() {
    return fixture.debugElement.query(By.css('.times-col-right'));
  }
});
