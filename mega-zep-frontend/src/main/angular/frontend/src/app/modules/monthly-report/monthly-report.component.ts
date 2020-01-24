import { Component, OnDestroy, OnInit } from '@angular/core';
import { MonthlyReport } from './models/MonthlyReport';
import { Subscription } from 'rxjs';
import { MonthlyReportService } from './services/monthly-report.service';

@Component({
  selector: 'app-monthly-report',
  templateUrl: './monthly-report.component.html'
})
export class MonthlyReportComponent implements OnInit, OnDestroy {

  private monthlyReportSubscrition: Subscription;
  monthlyReport: MonthlyReport;

  constructor(private monthlyReportService: MonthlyReportService) {
  }

  ngOnInit(): void {
    this.getAllTimeEntries();
  }

  ngOnDestroy(): void {
    if (this.monthlyReportSubscrition) {
      this.monthlyReportSubscrition.unsubscribe();
    }
  }

  getAllTimeEntries() {
    this.monthlyReportSubscrition = this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
      this.monthlyReport = monthlyReport;
    });
  }
}
