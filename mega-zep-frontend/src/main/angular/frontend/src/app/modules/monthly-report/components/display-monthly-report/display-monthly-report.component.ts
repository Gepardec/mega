import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {State} from '../../../shared/models/State';
import {TranslateService} from '@ngx-translate/core';
import {Subscription, zip} from "rxjs";
import {tap} from "rxjs/operators";
import * as _moment from "moment";
import {Moment} from "moment";
import {OfficeManagementService} from '../../../office-management/services/office-management.service';
import {MonthlyReportService} from '../../services/monthly-report.service';

const moment = _moment;

@Component({
  selector: 'app-display-monthly-report',
  templateUrl: './display-monthly-report.component.html',
  styleUrls: ['./display-monthly-report.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DisplayMonthlyReportComponent implements OnInit {

  selectedYear: number;
  selectedMonth: number;
  dateSelectionSub: Subscription;
  maxMonthDate: number = 0;

  readonly State = State;
  employeeFunctions;
  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();

  constructor(private translateService: TranslateService,
              private omService: OfficeManagementService,
              private monthlyReportService: MonthlyReportService) {
  }

  get date() {
    return moment()
      .year(this.selectedYear)
      .month(this.selectedMonth)
      .date(1)
      .startOf('day');
  }

  ngOnInit() {
    this.translateService.get('EMPLOYEE_FUNCTIONS').subscribe(funcs => this.employeeFunctions = funcs);

    this.dateSelectionSub = zip(this.monthlyReportService.selectedYear, this.monthlyReportService.selectedMonth)
      .pipe(
        tap(value => {
          this.selectedYear = value[0];
          this.selectedMonth = value[1] + 1;
        })
      ).subscribe();

  }

  isValidDate(dateStr: string): boolean {
    const date = new Date(dateStr);
    return date.getTime() === date.getTime();
  }

  emitRefreshMonthlyReport() {
    this.refreshMonthlyReport.emit();
  }

  dateChanged(date: Moment) {
    this.monthlyReportService.selectedYear.next(moment(date).year());
    this.monthlyReportService.selectedMonth.next(moment(date).month());
    this.emitRefreshMonthlyReport();
  }
}
