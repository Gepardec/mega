import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../../models/MonthlyReport';
import { State } from '../../../shared/models/State';

@Component({
  selector: 'app-time-check',
  templateUrl: './time-check.component.html',
  styleUrls: ['./time-check.component.scss']
})
export class TimeCheckComponent implements OnInit {
  @Input() monthlyReport: MonthlyReport;
  displayedColumns = ['dateTime', 'restTime', 'breakTime', 'workingTime'];
  State = State;

  constructor() {
  }

  ngOnInit(): void {
  }

}
