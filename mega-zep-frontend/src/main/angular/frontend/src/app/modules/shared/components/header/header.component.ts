import { Component, OnDestroy, OnInit } from '@angular/core';
import { configuration } from "../../constants/configuration";
import { UserService } from '../../services/user/user.service';
import { Subscription } from 'rxjs';
import { User } from '../../models/User';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  private userSubscription: Subscription;

  readonly links = [
    [configuration.PAGE_NAMES.MONTHLY_REPORT, configuration.PAGE_URLS.MONTHLY_REPORT],
    [configuration.PAGE_NAMES.EMPLOYEES, configuration.PAGE_URLS.EMPLOYEES]
  ];
  readonly spreadSheetUrl = configuration.SPREADSHEET_URL;

  user: User;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userSubscription = this.userService.user.subscribe((user) => this.user = user);
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  onLogout($event: void) {
    this.userService.logout();
  }
}
