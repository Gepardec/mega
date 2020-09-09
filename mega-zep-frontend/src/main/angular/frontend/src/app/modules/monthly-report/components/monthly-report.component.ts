import { Component, OnDestroy, OnInit } from '@angular/core';
import { MonthlyReport } from '../models/MonthlyReport';
import { Subscription } from 'rxjs';
import { MonthlyReportService } from '../services/monthly-report.service';
import { State } from '../../shared/models/State';

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
    // this.monthlyReportSubscription = this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
    //   this.monthlyReport = monthlyReport;
    // });
    this.monthlyReport = {
      employeeCheckState: State.DONE,
      otherChecksDone: false,
      employee: {
        firstName: 'Mock',
        sureName: 'Other',
        workDescription: '02',
        email: 'someemail@example.com',
        role: 1,
        salutation: 'Mister',
        title: 'Mag.',
        userId: '000-mother',
        active: true,
        releaseDate: '2020-02-02'
      },
      comments: [
        {message: 'wrong time', id: 12, creationDate: '2020-02-02', state: State.OPEN, author: 'some author'},
        {message: 'wrong time', id: 13, creationDate: '2020-02-02', state: State.DONE, author: 'some author'},
      ],
      timeWarnings: [
        // {
        //   warnings: ['1', '2'],
        //   missingRestTime: 12,
        //   missingBreakTime: 12,
        //   excessWorkTime: 12,
        //   date: new Date().toString()
        // }
      ],
      journeyWarnings: [
        // {warnings: ['1', '2'], date: new Date().toString()}
      ]
    };
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
