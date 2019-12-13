import {Component, OnDestroy, OnInit} from '@angular/core';
import {SocialUser} from 'angularx-social-login';
import {configuration} from '../../../configuration/configuration';
import {AuthenticationService} from '../zep-signin/authentication.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

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
    private authenticationService: AuthenticationService,
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
