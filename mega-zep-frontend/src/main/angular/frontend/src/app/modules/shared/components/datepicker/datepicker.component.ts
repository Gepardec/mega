import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { MatDatepicker, MatDatepickerInputEvent } from '@angular/material/datepicker';
import { formatDate } from '@angular/common';
import { configuration } from '../../constants/configuration';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent implements OnInit {

  today: Date = new Date();
  @ViewChild('picker') datePicker: MatDatepicker<Date>;
  @Output() dateEmitter: EventEmitter<string> = new EventEmitter<string>();
  selectedDate: string;
  private format = configuration.dateFormat;

  constructor() {
  }

  ngOnInit() {

  }

  emitEvent(date: string): void {
    this.dateEmitter.emit(date);
  }

  onDateChange($event: MatDatepickerInputEvent<Date & string, (Date & string) | null>) {
    const date: string = formatDate($event.value, this.format, 'en-US');
    this.emitEvent(date);
  }
}
