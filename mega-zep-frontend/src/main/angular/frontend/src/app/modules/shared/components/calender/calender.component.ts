import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { formatDate } from '@angular/common';
import { configuration } from '../../constants/configuration';

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.scss']
})
export class CalenderComponent implements OnInit {

  @Input() date: string;
  @Output() dateEmitter: EventEmitter<string> = new EventEmitter<string>();
  private format = configuration.dateFormat;

  ngOnInit(): void {
  }

  onSelect(event) {
    const date: string = formatDate(event, this.format, 'en-US');
    this.date = date;
    this.emitEvent(date);
  }

  emitEvent(date: string): void {
    this.dateEmitter.emit(date);
  }
}
