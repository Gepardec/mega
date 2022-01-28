import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatDatepicker, MatDatepickerInputEvent} from '@angular/material/datepicker';
import {formatDate} from '@angular/common';
import {configuration} from '../../constants/configuration';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent implements OnInit {

  today: Date = new Date();
  @ViewChild('picker') datePicker: MatDatepicker<Date>;
  @Output() dateEmitter: EventEmitter<string> = new EventEmitter<string>();
  @Input() selectedDate: string;
  private format = configuration.dateFormat;

  constructor() {
  }

  ngOnInit() {
  }

  emitEvent(date: string): void {
    this.dateEmitter.emit(date);
  }

  getDateAndEmitEvent(event: MatDatepickerInputEvent<Date>){
    const date: string = formatDate(event.value, this.format, 'en-US');
    this.emitEvent(date);
  }
}
