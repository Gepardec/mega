import {Component, OnInit} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private oAuthService: OAuthService,
              private userService: UserService) {
  }

  ngOnInit(): void {
  }

  login(): void {
    this.oAuthService.initLoginFlow();
  }

  loggedIn() {
    return this.userService.loggedInWithGoogle();
  }
}
