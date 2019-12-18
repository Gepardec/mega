import {Component, OnDestroy, OnInit} from '@angular/core';
import {MonthlyReport} from "../../../shared/models/MonthlyReport/MonthlyReport";
import {SocialUser} from "angularx-social-login";
import {Subscription} from "rxjs";
import {ZepSigninService} from "../../../shared/services/signin/zep-signin.service";
import {MonthlyReportService} from "../../../shared/services/zep-services/monthly-report.service";

@Component({
  selector: 'app-monthly-report',
  templateUrl: './monthly-report.container.html'
})
export class MonthlyReportContainer implements OnInit, OnDestroy {

  user: SocialUser;
  private currentUserSubscription: Subscription;
  private monthlyReportSubscrition: Subscription;
  monthlyReport: MonthlyReport;

  constructor(
    private authenticationService: ZepSigninService,
    private monthlyReportService: MonthlyReportService
  ) {
  }

  ngOnInit() {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      this.getAllTimeEntries();
    });
  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
    this.monthlyReportSubscrition && this.monthlyReportSubscrition.unsubscribe();
  }

  getAllTimeEntries() {
    if (this.user) {
      this.monthlyReportSubscrition = this.monthlyReportService.getAll(this.user).subscribe((monthlyReport: MonthlyReport) => {
        this.monthlyReport = monthlyReport
      });
    }
  }
}
