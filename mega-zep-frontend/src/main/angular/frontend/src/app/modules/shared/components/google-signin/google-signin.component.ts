import {Component, OnDestroy, OnInit} from '@angular/core';
import {configuration} from "../../constants/configuration";
import {Subscription} from "rxjs";
import {ZepSigninService} from "../../services/signin/zep-signin.service";
import {Router} from "@angular/router";
import {SocialUser} from "angularx-social-login";

@Component({
  selector: 'app-google-signin',
  templateUrl: './google-signin.component.html',
  styleUrls: ['./google-signin.component.scss']
})
export class GoogleSigninComponent implements OnInit, OnDestroy {

  private readonly MONTHLY_REPORT: string = configuration.PAGE_URLS.MONTHLY_REPORT;

  private loggedIn = false;

  private authenticationServiceSubscription: Subscription;

  constructor(
    private authenticationService: ZepSigninService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.authenticationServiceSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.loggedIn = user != null;
      // route to employees if user is logged in and wants to access login page
      if (this.loggedIn) {
        this.router.navigate([this.MONTHLY_REPORT]);
      } else {
        this.signinWithGoogle();
      }
    });
  }

  ngOnDestroy(): void {
    this.authenticationServiceSubscription && this.authenticationServiceSubscription.unsubscribe();
  }

  signinWithGoogle(): void {
    this.authenticationService.signinWithGoogle();
  }

  signOut(): void {
    this.authenticationService.signOut();
  }
}
