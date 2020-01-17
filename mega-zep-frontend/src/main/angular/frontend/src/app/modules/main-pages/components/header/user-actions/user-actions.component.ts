import {Component, OnDestroy, OnInit} from '@angular/core';
import {ZepSigninService} from "../../../../shared/services/signin/zep-signin.service";
import {SocialUser} from "angularx-social-login";
import {Subscription} from "rxjs";
import {User} from "../../../../shared/models/User/User";

@Component({
  selector: 'app-user-actions',
  templateUrl: './user-actions.component.html',
  styleUrls: ['./user-actions.component.scss']
})
export class UserActionsComponent implements OnInit, OnDestroy {

  user: User;

  private authenticationServiceSubscription: Subscription;

  constructor(
    private authenticationService: ZepSigninService
  ) {
  }

  ngOnInit() {
    this.authenticationServiceSubscription = this.authenticationService.currentEmployee.subscribe((user: User) => {
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    this.authenticationServiceSubscription && this.authenticationServiceSubscription.unsubscribe();
  }

  logout() {
    this.authenticationService.logout();
  }
}
