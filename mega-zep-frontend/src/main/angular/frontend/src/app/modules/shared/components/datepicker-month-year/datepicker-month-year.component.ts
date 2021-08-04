import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';

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
    {provide: MAT_DATE_LOCALE, useValue: 'de-AT'}
  ]
})
export class DatepickerMonthYearComponent implements OnChanges, OnInit {


  @ViewChild('picker') datePicker: MatDatepicker<any>;
  @Input() date: Moment;
  @Input() maxMonth: number;
  @Output() dateChanged: EventEmitter<Moment> = new EventEmitter<Moment>();

  pickerDate: MatDatepicker<any>;
  dateControl = new FormControl(moment().subtract(1, 'month'));
  maxDate = moment().subtract(0, 'month').format('YYYY-MM-DD');

  chosenYearHandler(normalizedYear: Moment) {
    const ctrlValue = this.dateControl.value;
    ctrlValue.year(normalizedYear.year());
    this.dateControl.setValue(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<any>) {
    const ctrlValue = this.dateControl.value;
    ctrlValue.month(normalizedMonth.month());
    this.dateControl.setValue(ctrlValue);
    datepicker.close();

    const changedDate = moment(this.dateControl.value);
    this.dateChanged.emit(changedDate);
  }

  ngOnInit() {
    this.maxDate = moment().subtract(this.maxMonth, 'month').format('YYYY-MM-DD');
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.dateControl.setValue(this.date.subtract(1, 'month'));
  }
}
