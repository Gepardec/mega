import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatDatepicker} from '@angular/material/datepicker';
import {formatDate} from '@angular/common';
import {configuration} from '../../constants/configuration';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent implements OnInit, AfterViewInit {

  today: Date = new Date();
  @ViewChild('picker') datePicker: MatDatepicker<Date>;
  @Output() dateEmitter: EventEmitter<string> = new EventEmitter<string>();
  @Input() selectedDate: string;
  private format = configuration.dateFormat;

  constructor() {
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.datePicker._datepickerInput._valueChange
      .subscribe((value: Date) => {
        const date: string = formatDate(value, this.format, 'en-US');
        this.emitEvent(date);
      });
  }

  emitEvent(date: string): void {
    this.dateEmitter.emit(date);
  }
}
