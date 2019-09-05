import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {formatDate} from "@angular/common";
import {configuration} from "../../../../configuration/configuration";

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.scss']
})
export class CalenderComponent implements OnInit {

  private format = configuration.dateFormat;
  private today: Date = new Date();

  @Output("dateEmitter") dateEmitterEvent: EventEmitter<string> = new EventEmitter<string>();
  @Input("date") selectedDate: string;

  ngOnInit(): void {
  }

  onSelect(event) {
    let date: string = formatDate(event, this.format, 'en-US');
    this.selectedDate = date;
    this.emitEvent(date);
  }

  emitEvent(date: string): void {
    this.dateEmitterEvent.emit(date);
  }

}
