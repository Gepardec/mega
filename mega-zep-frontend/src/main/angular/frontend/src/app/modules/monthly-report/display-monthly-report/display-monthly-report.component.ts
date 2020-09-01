import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../models/MonthlyReport';
import { configuration } from '../../shared/constants/configuration';

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss']
})
export class DisplayMonthlyReportComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  displayedColumnsTimeTable = ['dateTime', 'restTime', 'breakTime', 'workingTime'];
  displayedColumnsJourneyTable = ['dateJourney', 'warningJourney'];
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  constructor() {
  }

  ngOnInit() {
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

  formatAuthorName(author: string): string {
    const foreAndSureNameArr = author.substring(0, author.indexOf('@')).split('.');

    let foreName = foreAndSureNameArr[0];
    let sureName = foreAndSureNameArr[1];

    foreName = foreName.charAt(0).toUpperCase() + foreName.slice(1);
    sureName = sureName.charAt(0).toUpperCase() + sureName.slice(1);

    return foreName + ' ' + sureName;
  }
}
