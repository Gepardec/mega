import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../../signin/authentication.service";
import {SocialUser} from "angularx-social-login";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-user-actions',
  templateUrl: './user-actions.component.html',
  styleUrls: ['./user-actions.component.scss']
})
export class UserActionsComponent implements OnInit, OnDestroy {

  user: SocialUser;

  private authenticationServiceSubscription: Subscription;

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
   this.authenticationServiceSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
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
