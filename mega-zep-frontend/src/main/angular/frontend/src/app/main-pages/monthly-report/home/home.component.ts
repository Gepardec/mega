import {Component, OnDestroy, OnInit} from '@angular/core';
import {TimeEntry} from "../../../models/MonthlyReport/TimeEntry";
import {MatTableDataSource} from "@angular/material/table";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {TimeEntryService} from "../time-entry.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {

  user: SocialUser;
  private currentUserSubscription: Subscription;
  private timeEntries: Array<TimeEntry>;
  public dataSource = new MatTableDataSource<TimeEntry>();

  constructor(
    private authenticationService: AuthenticationService,
    private timeEntryService: TimeEntryService
  ) {
  }

  ngOnInit() {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      this.getAllTimeEntries();
    });
    this.dataSource.data = this.timeEntries;
  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
  }

  getAllTimeEntries() {
    if (this.user) {
      this.timeEntries = this.timeEntryService.getAll(this.user);
    }
  }

}
