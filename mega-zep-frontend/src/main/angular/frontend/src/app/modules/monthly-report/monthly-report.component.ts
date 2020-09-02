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
    // this.monthlyReport = {
    //   emc: 'OPEN',
    //   comments: [
    //     {
    //       message: '1',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '2aaaaaaaaaaaaa asdfjkhasdf asdfdöfjkhasdf asjködfhas dfaskdjfh asdfh as as as as asa sas as asa sas ',
    //       author: 'werner.bruckmueller@gepardec.com',
    //       creationDate: '2020-08-05T00:00',
    //       state: 'DONE'
    //     },
    //     {
    //       message: '3',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '4',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '5',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '6',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '7',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '8',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '9',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '10',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '11',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '12',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '13',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '14',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '15',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '16',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '17',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '18',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '19',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     },
    //     {
    //       message: '20',
    //       author: 'evelyn.pirklbauer@gepardec.com',
    //       creationDate: '2020-08-03T00:00',
    //       state: 'OPEN'
    //     }
    //   ],
    //   timeWarnings: [
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //     {date: new Date(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
    //   ],
    //   journeyWarnings: [
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {
    //       date: new Date(),
    //       warnings: ['peafafas', 'kopf', 'aasdfasdfaasdfasdfaasdfasdfaasdfasdfaa']
    //     },
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']},
    //     {date: new Date(), warnings: ['peafafas', 'kopf', 'aasdfasdf']}
    //   ],
    //   employee: {
    //     userId: '054-maslan',
    //     firstName: 'Mario',
    //     sureName: 'Aslan',
    //     salutation: null,
    //     releaseDate: '2020-07-01',
    //     workDescription: '05',
    //     email: 'mario.aslan@gepardec.com',
    //     role: 1,
    //     title: null,
    //     active: true
    //   }
    // };
    this.monthlyReportService.getAll().subscribe((monthlyReport: MonthlyReport) => {
      this.monthlyReport = monthlyReport;
    });
  }
}
