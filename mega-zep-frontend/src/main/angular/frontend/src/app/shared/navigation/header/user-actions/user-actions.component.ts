import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../../../signin/authentication.service";
import {SocialUser} from "angularx-social-login";

@Component({
  selector: 'app-user-actions',
  templateUrl: './user-actions.component.html',
  styleUrls: ['./user-actions.component.scss']
})
export class UserActionsComponent implements OnInit {

  protected user: SocialUser;

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
    });
  }

  logout() {
    this.authenticationService.signOut();
  }

}
