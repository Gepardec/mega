import {Component} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {UserService} from '../../services/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(private oAuthService: OAuthService,
              private userService: UserService) {
  }

  login(): void {
    this.oAuthService.initLoginFlow();
  }

  loggedIn(): boolean {
    return this.userService.loggedInWithGoogle();
  }
}
