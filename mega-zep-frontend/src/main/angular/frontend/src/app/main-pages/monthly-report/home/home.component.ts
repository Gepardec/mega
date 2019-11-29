import {Component, OnDestroy, OnInit} from '@angular/core';
import {MonthlyReport} from "../../../models/MonthlyReport/MonthlyReport";
import {MatTableDataSource} from "@angular/material/table";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {Subscription} from "rxjs";
import {MonthlyReportService} from "../../../zep-services/monthly-report.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {

  user: SocialUser;
  private currentUserSubscription: Subscription;
  private monthlyReport: MonthlyReport;

  constructor(
    private authenticationService: AuthenticationService,
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
  }

  getAllTimeEntries() {
    if (this.user) {
      this.monthlyReport = this.monthlyReportService.getAll(this.user);
    }
  }

}
