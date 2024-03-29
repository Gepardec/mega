import {Component, OnInit} from '@angular/core';
import {MonthlyReport} from '../models/MonthlyReport';
import {Subscription} from 'rxjs';
import {MonthlyReportService} from '../services/monthly-report.service';

@Component({
  selector: 'app-monthly-report',
  templateUrl: './monthly-report.component.html'
})
export class MonthlyReportComponent implements OnInit {

  public monthlyReport: MonthlyReport;
  private monthlyReportSubscription: Subscription;

  constructor(private monthlyReportService: MonthlyReportService) {
  }

  ngOnInit(): void {
    this.getAllTimeEntries();
  }

  getAllTimeEntriesByDate(year: number, month: number): void {
    this.monthlyReportSubscription = this.monthlyReportService.getAllByDate(year, month).subscribe((monthlyReport: MonthlyReport) => {
      this.monthlyReport = monthlyReport;
    });
  }

  getAllTimeEntries(): void {
    this.monthlyReportSubscription = this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
      if (monthlyReport) {
        this.monthlyReport = monthlyReport;
        const splitReleaseDate = this.monthlyReport.employee.releaseDate.split('-');
        this.monthlyReportService.selectedYear.next(+splitReleaseDate[0]);
        this.monthlyReportService.selectedMonth.next(+splitReleaseDate[1]);
      }
    });
  }

  refreshMonthlyReport(): void {
    // trigger skeleton loaders
    this.monthlyReport = null;
    this.getAllTimeEntriesByDate(this.monthlyReportService.selectedYear.getValue(), this.monthlyReportService.selectedMonth.getValue());
  }
}
