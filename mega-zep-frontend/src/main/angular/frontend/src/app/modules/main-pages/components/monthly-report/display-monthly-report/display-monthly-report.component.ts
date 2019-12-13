import {Component, Input, OnInit} from '@angular/core';
import {MonthlyReport} from "../../../../shared/models/MonthlyReport/MonthlyReport";
import {MatTableDataSource} from "@angular/material/table";
import {TimeWarning} from "../../../../shared/models/MonthlyReport/TimeWarning";
import {JourneyWarning} from "../../../../shared/models/MonthlyReport/JourneyWarning";
import {configuration} from "../../../../../../configuration/configuration";

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
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  constructor() {  }

  ngOnInit() {
    if(this.monthlyReport) {
      this.datasourceTimeTable.data = this.monthlyReport.timeWarnings;
      this.datasourceJourneyTable.data = this.monthlyReport.journeyWarnings;
    }
  }

  getDateOfReport(date: string): Date {
    let reportDate = new Date(date);
    reportDate.setMonth(reportDate.getMonth() + 1);
    return reportDate;
  }
}
