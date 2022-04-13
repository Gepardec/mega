import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {BillableTimesComponent} from './billable-times.component';
import {By} from '@angular/platform-browser';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {AngularMaterialModule} from "../../../material/material-module";

describe('BillableTimesComponent', () => {

  let component: BillableTimesComponent;
  let fixture: ComponentFixture<BillableTimesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        BillableTimesComponent
      ],
      imports: [
        AngularMaterialModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(BillableTimesComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#transformTimeToFractionNumber - should display (non-)billable times and transform them to fraction number', () => {
    component.billableTimes = '80:30';
    component.nonBillableTimes = '04:15';

    fixture.detectChanges();

    expect(billableTimesValue()).toEqual('80,50');
    expect(nonBillableTimesValue()).toEqual('4,25');
  });

  it('#transformTimeToFractionNumber - should display horizontal rule if (non-)billable times are "00:00"', () => {
    component.billableTimes = '00:00';
    component.nonBillableTimes = '00:00';

    fixture.detectChanges();

    const billableTimesMatIcon = billableTimesElement().children[0];
    expect(billableTimesMatIcon.name).toEqual('mat-icon');
    expect(billableTimesMatIcon.nativeElement.innerHTML).toEqual('horizontal_rule');

    const nonBillableTimesMatIcon = nonBillableTimesElement().children[0];
    expect(nonBillableTimesMatIcon.name).toEqual('mat-icon');
    expect(nonBillableTimesMatIcon.nativeElement.innerHTML).toEqual('horizontal_rule');
  });

  it('#transformTimeToFractionNumber - should display dollar coin icon on the left side', () => {
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
