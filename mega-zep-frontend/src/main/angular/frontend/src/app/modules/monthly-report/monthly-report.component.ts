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

  ngOnInit(): void {
    this.getAllTimeEntries();
  }

  ngOnDestroy(): void {
    if (this.monthlyReportSubscription) {
      this.monthlyReportSubscription.unsubscribe();
    }
  }

  getAllTimeEntries() {
    this.monthlyReport = {
      emc: 'DONE',
      comments: [
        {
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
        {
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
{
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },
        {
          message: 'Evelyn für Mario 1',
          author: 'evelyn.pirklbauer@gepardec.com',
          creationDate: '2020-08-03T00:00',
          state: 'OPEN'
        },
        {
          message: 'Werner für Mario 1',
          author: 'werner.bruckmueller@gepardec.com',
          creationDate: '2020-08-05T00:00',
          state: 'DONE'
        },

      ],
      timeWarnings: [],
      journeyWarnings: [
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {
          date: new Date(),
          warnings: ['peafafas', 'kopf', 'aasdfasdfaasdfasdfaasdfasdfaasdfasdfaa']
        },
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
        {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']}
      ],
      employee: {
        userId: '054-maslan',
        firstName: 'Mario',
        sureName: 'Aslan',
        salutation: null,
        releaseDate: '2020-07-01',
        workDescription: '05',
        email: 'mario.aslan@gepardec.com',
        role: 1,
        title: null,
        active: true
      }
    };
    // this.monthlyReportSubscription = this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
    //
    //   console.log(JSON.stringify(monthlyReport));
    //   this.monthlyReport = monthlyReport;
    // });
  }
}
