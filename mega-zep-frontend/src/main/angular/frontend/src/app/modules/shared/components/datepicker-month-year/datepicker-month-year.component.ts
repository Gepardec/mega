import {Component, EventEmitter, Output, ViewChild} from '@angular/core';

import {FormControl} from '@angular/forms';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MatDatepicker} from '@angular/material/datepicker';

import * as _moment from 'moment';
import {Moment} from 'moment';

const moment = _moment;

export const MY_FORMATS = {
  parse: {
    dateInput: 'YYYY-MM',
  },
  display: {
    dateInput: 'MMMM YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-datepicker-month-year',
  templateUrl: './datepicker-month-year.component.html',
  styleUrls: ['./datepicker-month-year.component.scss'],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class DatepickerMonthYearComponent {

  @ViewChild('picker') datePicker: MatDatepicker<any>;
  @Output() dateChanged: EventEmitter<Moment> = new EventEmitter<Moment>();

  pickerDate: MatDatepicker<any>;
  date = new FormControl(moment().subtract(1, 'month'));
  maxDate = moment().subtract(1, 'month').format('YYYY-MM-DD');

  chosenYearHandler(normalizedYear: Moment) {
    const ctrlValue = this.date.value;
    ctrlValue.year(normalizedYear.year());
    this.date.setValue(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<any>) {
    const ctrlValue = this.date.value;
    ctrlValue.month(normalizedMonth.month());
    this.date.setValue(ctrlValue);
    datepicker.close();

    const changedDate = moment(this.date.value);
    this.dateChanged.emit(changedDate);
  }
}
