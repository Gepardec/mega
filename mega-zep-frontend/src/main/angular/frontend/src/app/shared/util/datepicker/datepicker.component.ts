import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatDatepicker} from "@angular/material";
import {formatDate} from "@angular/common";
import {configuration} from "../../../../configuration/configuration";

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent implements OnInit, AfterViewInit {

  private format = configuration.dateFormat;
  today: Date = new Date();

  @ViewChild('picker', {static: false}) datePicker: MatDatepicker<Date>;
  @Output("dateEmitter") dateEmitterEvent: EventEmitter<string> = new EventEmitter<string>();
  selectedDate: string;

  constructor() {
  }

  ngOnInit() {

  }

  ngAfterViewInit(): void {
    this.datePicker._datepickerInput._valueChange
      .subscribe((value: Date) => {
        let date: string = formatDate(value, this.format, 'en-US');
        this.emitEvent(date);
      });
  }

  emitEvent(date: string): void {
    this.dateEmitterEvent.emit(date);
  }

}
