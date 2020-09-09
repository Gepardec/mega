import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../../models/MonthlyReport';
import { configuration } from '../../../shared/constants/configuration';
import { State } from '../../../shared/models/State';
import { MatSelectionListChange } from '@angular/material/list';

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DisplayMonthlyReportComponent implements OnInit {

  readonly State = State;
  readonly FUNCTIONS = configuration.EMPLOYEE_FUNCTIONS;

  @Input() monthlyReport: MonthlyReport;

  displayedColumnsTimeTable = ['dateTime', 'restTime', 'breakTime', 'workingTime'];
  displayedColumnsJourneyTable = ['dateJourney', 'warningJourney'];

  constructor() {
  }

  ngOnInit() {
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
      if (comment.state !== this.State.DONE) {
        return false;
      }
    }
    return true;
  }

  selectionChange(change: MatSelectionListChange): void {
    const comment = change.option.value;
    const selected = change.option.selected;
    if (selected) {
      // TODO: Server request
    }
  }
}
