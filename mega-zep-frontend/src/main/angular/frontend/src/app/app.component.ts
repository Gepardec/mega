import { Component, OnDestroy, OnInit } from '@angular/core';
import { JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from './auth/auth.config';
import { Router } from '@angular/router';
import { UserService } from './modules/shared/services/user/user.service';
import { ConfigService } from './modules/shared/services/config/config.service';
import { Config } from './modules/shared/models/Config';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit, OnDestroy {

  private configServiceSubscription: Subscription;

  constructor(private router: Router,
              private oAuthService: OAuthService,
              private configService: ConfigService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.configServiceSubscription = this.configService.getConfig().subscribe((config: Config) => {
      this.oAuthService.configure({
        clientId: config.clientId,
        issuer: config.issuer,
        scope: config.scope,
        ...authConfig
      });
      this.oAuthService.tokenValidationHandler = new JwksValidationHandler();

      this.oAuthService.loadDiscoveryDocumentAndTryLogin().then((result: boolean) => {
        if (this.userService.loggedInWithGoogle()) {
          this.userService.loginUser();
        }
      });
    });
  }

  ngOnDestroy(): void {
    this.configServiceSubscription.unsubscribe();
  }
}
