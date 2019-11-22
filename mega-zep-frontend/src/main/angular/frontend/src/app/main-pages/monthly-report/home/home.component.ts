import {Component, OnDestroy, OnInit} from '@angular/core';
import {TimeEntry} from "../../../models/MonthlyReport/TimeEntry";
import {MatTableDataSource} from "@angular/material/table";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {DisplayTimeEntryService} from "../display-time-entry/display-time-entry.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  user: SocialUser;
  private currentUserSubscription: Subscription;
  private timeEntries: Array<TimeEntry>;
  public dataSource = new MatTableDataSource<TimeEntry>();

  constructor(
    private authenticationService: AuthenticationService,
    private displayTimeEntryService: DisplayTimeEntryService
  ) {
  }

  ngOnInit() {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      this.getAllEmployees();
    });
    this.dataSource.data = this.timeEntries;
  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
  }

  getAllEmployees() {
    if (this.user) {
      this.timeEntries = this.displayTimeEntryService.getAll(this.user);
    }
  }

}
