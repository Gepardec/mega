import { Component, OnDestroy, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import {authConfig, cypressAuthConfig} from './auth/auth.config';
import { Router } from '@angular/router';
import { UserService } from './modules/shared/services/user/user.service';
import { ConfigService } from './modules/shared/services/config/config.service';
import { Config } from './modules/shared/models/Config';
import { Subscription } from 'rxjs';
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';
import { TranslateService } from '@ngx-translate/core';

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
              private userService: UserService,
              private translate: TranslateService) {
    translate.addLangs(['de']);
    translate.setDefaultLang('de');
  }

  ngOnInit(): void {
    this.configServiceSubscription = this.configService.getConfig().subscribe((config: Config) => {
      this.oAuthService.configure({
        clientId: config.clientId,
        issuer: config.issuer,
        scope: config.scope,
        // @ts-ignore
        ...(window.Cypress ? cypressAuthConfig : authConfig)
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
    if (this.configServiceSubscription) {
      this.configServiceSubscription.unsubscribe();
    }
  }
}
