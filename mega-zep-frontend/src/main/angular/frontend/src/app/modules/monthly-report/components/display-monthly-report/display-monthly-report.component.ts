import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {State} from '../../../shared/models/State';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DisplayMonthlyReportComponent implements OnInit {
  readonly State = State;
  employeeFunctions;
  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();

  constructor(private translateService: TranslateService) {
  }

  ngOnInit() {
    this.translateService.get('EMPLOYEE_FUNCTIONS').subscribe(funcs => this.employeeFunctions = funcs);
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

  emitRefreshMonthlyReport() {
    this.refreshMonthlyReport.emit();
  }
}
