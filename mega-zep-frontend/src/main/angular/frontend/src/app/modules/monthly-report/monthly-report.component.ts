import { Component, OnDestroy, OnInit } from '@angular/core';
import { MonthlyReport } from './models/MonthlyReport';
import { Subscription } from 'rxjs';
import { MonthlyReportService } from './services/monthly-report.service';

@Component({
  selector: 'app-monthly-report',
  templateUrl: './monthly-report.component.html'
})
export class MonthlyReportComponent implements OnInit, OnDestroy {
  monthlyReport: MonthlyReport;
  private monthlyReportSubscription: Subscription;

  constructor(private monthlyReportService: MonthlyReportService) {
  }

  getAllTimeEntries() {
    this.monthlyReportSubscription = this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
      this.monthlyReport = monthlyReport;
    });
  }

  ngOnInit(): void {
    this.getAllTimeEntries();
  }

  ngOnDestroy(): void {
    if (this.monthlyReportSubscription) {
      this.monthlyReportSubscription.unsubscribe();
    }
  }
}
