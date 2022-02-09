import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {MatDatepicker, MatDatepickerInputEvent} from '@angular/material/datepicker';
import {formatDate} from '@angular/common';
import {configuration} from '../../constants/configuration';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent {

  @Input() selectedDate: string;
  @Output() dateEmitter: EventEmitter<string> = new EventEmitter<string>();
  @ViewChild('picker') datePicker: MatDatepicker<Date>;

  today: Date = new Date();

  private format = configuration.dateFormat;

  emitEvent(date: string): void {
    this.dateEmitter.emit(date);
  }

  getDateAndEmitEvent(event: MatDatepickerInputEvent<Date>) {
    const date: string = formatDate(event.value, this.format, 'en-US');
    this.emitEvent(date);
  }
}
