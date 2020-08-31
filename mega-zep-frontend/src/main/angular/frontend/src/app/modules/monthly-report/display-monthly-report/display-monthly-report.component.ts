import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../models/MonthlyReport';
import { MatTableDataSource } from '@angular/material/table';
import { TimeWarning } from '../models/TimeWarning';
import { JourneyWarning } from '../models/JourneyWarning';
import { configuration } from '../../shared/constants/configuration';
import { MatSelectionListChange } from '@angular/material/list';

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss']
})
export class DisplayMonthlyReportComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  displayedColumnsTimeTable = ['date-time', 'restTime', 'breakTime', 'workingTime'];
  displayedColumnsJourneyTable = ['date-journey', 'warning'];
  datasourceTimeTable = new MatTableDataSource<TimeWarning>();
  datasourceJourneyTable = new MatTableDataSource<JourneyWarning>();
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  constructor() {
  }

  ngOnInit() {
    this.datasourceTimeTable.data = this.monthlyReport.timeWarnings;
    this.datasourceJourneyTable.data = this.monthlyReport.journeyWarnings;
  }

  getJourneyWarningString(warnings: Array<string>): string {
    let warningString = '';
    warnings.forEach((value) => warningString += value + '. ');
    return warningString;
  }

  getDateOfReport(date: string): Date {
    const reportDate = new Date(date);
    reportDate.setDate(1);
    reportDate.setMonth(reportDate.getMonth() + 1);
    return reportDate;
  }

  areAllCommentsDone(): boolean {
    for (const comment of this.monthlyReport.comments) {
      if (comment.state === 'OPEN') {
        return false;
      }
    }
    return true;
  }
}
