import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {BillableTimesFractionComponent} from './billable-times-fraction.component';
import {By} from '@angular/platform-browser';
import {expect} from '@angular/flex-layout/_private-utils/testing';

describe('BillableTimesFractionComponent', () => {

  let component: BillableTimesFractionComponent;
  let fixture: ComponentFixture<BillableTimesFractionComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({}).compileComponents().then(() => {
      fixture = TestBed.createComponent(BillableTimesFractionComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should display (non-)billable times as fraction numbers', () => {
    component.billableTimes = 14.5;
    component.nonBillableTimes = 1;

    fixture.detectChanges();

    expect(billableTimesValue()).toEqual('14,50');
    expect(nonBillableTimesValue()).toEqual('1,00');
  });

  it('#afterInit - should display horizontal rule if billable times are 0', () => {
    component.billableTimes = 0;
    component.nonBillableTimes = 0;

    fixture.detectChanges();

    const billableTimesMatIcon = billableTimesElement().children[0];
    expect(billableTimesMatIcon.name).toEqual('mat-icon');
    expect(billableTimesMatIcon.nativeElement.innerHTML).toEqual('horizontal_rule');

    const nonBillableTimesMatIcon = nonBillableTimesElement().children[0];
    expect(nonBillableTimesMatIcon.name).toEqual('mat-icon');
    expect(nonBillableTimesMatIcon.nativeElement.innerHTML).toEqual('horizontal_rule');
  });

  it('#afterInit - should display dollar coin icon on the left side', () => {
    const dollarIcon = fixture.debugElement.query(By.css('.billable'));
    expect(dollarIcon.name).toEqual('mat-icon');
    expect(dollarIcon.nativeElement.innerHTML).toEqual('monetization_on');
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
