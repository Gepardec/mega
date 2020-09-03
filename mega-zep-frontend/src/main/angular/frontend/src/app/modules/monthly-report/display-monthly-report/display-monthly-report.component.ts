import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../models/MonthlyReport';
import { configuration } from '../../shared/constants/configuration';

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DisplayMonthlyReportComponent implements OnInit {

  readonly DONE = 'DONE';
  readonly OPEN = 'OPEN';
  readonly FUNCTIONS = configuration.EMPLOYEE_FUNCTIONS;

  @Input() monthlyReport: MonthlyReport;

  displayedColumnsTimeTable = ['dateTime', 'restTime', 'breakTime', 'workingTime'];
  displayedColumnsJourneyTable = ['dateJourney', 'warningJourney'];

  constructor() {
  }

  ngOnInit() {
    console.log(this.monthlyReport.emcState);
    console.log(this.monthlyReport.comments.length);
  }

  getJourneyWarningString(warnings: Array<string>): string {
    let warningString = '';
    warnings.forEach((value) => warningString += value + '. ');
    return warningString;
  }

  getDateOfReport(dateStr: string): Date {
    const reportDate = new Date(dateStr);
    reportDate.setDate(1);
    reportDate.setMonth(reportDate.getMonth() + 1);
    return reportDate;
  }

  isValidDate(dateStr: string): boolean {
    const date = new Date(dateStr);
    return date.getTime() === date.getTime();
  }

  areAllCommentsDone(): boolean {
    for (const comment of this.monthlyReport.comments) {
      if (comment.state !== this.DONE) {
        return false;
      }
    }
    return true;
  }

  /* TODO: this method is likely to be deleted.
     As of now the email is used to determine the author of a comment.
     This prevents calling ZepService.getEmployee for every comment. */
  formatAuthorName(author: string): string {
    const foreAndSureNameArr = author.substring(0, author.indexOf('@')).split('.');

    let foreName = foreAndSureNameArr[0];
    let sureName = foreAndSureNameArr[1];

    foreName = foreName.charAt(0).toUpperCase() + foreName.slice(1);
    sureName = sureName.charAt(0).toUpperCase() + sureName.slice(1);

    return foreName + ' ' + sureName;
  }
}
