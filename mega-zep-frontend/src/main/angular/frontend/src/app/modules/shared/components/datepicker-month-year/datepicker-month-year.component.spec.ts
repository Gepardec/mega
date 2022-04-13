import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DatepickerMonthYearComponent} from './datepicker-month-year.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from '@angular/forms';

import * as _moment from "moment";
import {configuration} from "../../constants/configuration";

const moment = _moment;
const DATE_FORMAT: string = configuration.dateFormat;

describe('DatepickerMonthYearComponent', () => {

  let component: DatepickerMonthYearComponent;
  let fixture: ComponentFixture<DatepickerMonthYearComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        DatepickerMonthYearComponent
      ],
      imports: [
        AngularMaterialModule,
        BrowserAnimationsModule,
        ReactiveFormsModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(DatepickerMonthYearComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should set maxDate to today', () => {
    fixture.detectChanges();

    component.ngOnInit();

    expect(component.maxDate).toEqual(moment().format(DATE_FORMAT));
  });

  it('#onChanges - should set dateControl to date minus 1 month', () => {
    fixture.detectChanges();

    component.date = moment();
    component.ngOnChanges(null);

    expect(component.dateControl.value.format(DATE_FORMAT))
      .toEqual(moment().subtract(1, 'month').format(DATE_FORMAT));
  });

  it('#chosenYearHandler - should set normalized year', () => {
    fixture.detectChanges();

    component.dateControl.setValue(moment());
    component.chosenYearHandler(moment().subtract(1, 'year'));

    expect(component.dateControl.value.year()).toEqual(moment().subtract(1, 'year').year());
  });

  it('#chosenMonthHandler - should set normalized month', () => {
    fixture.detectChanges();

    spyOn(component.datePicker, 'close').and.stub();

    component.dateControl.setValue(moment());
    component.chosenMonthHandler(moment().subtract(1, 'year').subtract(1, 'month'), component.datePicker);

    expect(component.dateControl.value.year()).toEqual(moment().subtract(1, 'year').subtract(1, 'month').year());
    expect(component.datePicker.close).toHaveBeenCalled();
  });
});
