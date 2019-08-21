import {Component, OnInit} from '@angular/core';
import {SocialUser} from "angularx-social-login";
import {configuration} from "../../../configuration/configuration";
import {AuthenticationService} from "../authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-google-signin',
  templateUrl: './google-signin.component.html',
  styleUrls: ['./google-signin.component.scss']
})
export class GoogleSigninComponent implements OnInit {

  private readonly HOME: string = configuration.PAGES.HOME;

  private loggedIn: boolean = false;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.loggedIn = user != null;
      if (this.loggedIn) {
        this.router.navigate([this.HOME]);
      }
    });
  }

  signinWithGoogle(): void {
    this.authenticationService.signinWithGoogle();
  }

  signOut(): void {
    this.authenticationService.signOut();
  }

}
