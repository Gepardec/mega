import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MonthlyReport} from "../../../models/MonthlyReport/MonthlyReport";
import {TimeWarning} from "../../../models/MonthlyReport/TimeWarning";
import {JourneyWarning} from "../../../models/MonthlyReport/JourneyWarning";

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss']
})
export class DisplayMonthlyReportComponent implements OnInit {

  @Input('monthlyReport') monthlyReport: MonthlyReport;
  displayedColumnsTimeTable = ['date', 'restTime', 'breakTime', 'workingTime'];
  displayedColumnsJourneyTable = ['date', 'warning'];
  datasourceTimeTable = new MatTableDataSource<TimeWarning>();
  datasourceJourneyTable = new MatTableDataSource<JourneyWarning>();

  constructor() {  }

  ngOnInit() {
    this.datasourceTimeTable.data = this.monthlyReport.timeWarnings;
    this.datasourceJourneyTable.data = this.monthlyReport.journeyWarnings;
  }
}
