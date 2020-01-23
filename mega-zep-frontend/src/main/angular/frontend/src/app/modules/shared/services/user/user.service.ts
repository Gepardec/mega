import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "../config/config.service";
import { OAuthService } from "angular-oauth2-oidc";
import { User } from "../../models/User";
import { configuration } from '../../constants/configuration';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private SESSION_STORAGE_KEY: string = 'MEGA_USER';
  private SESSION_STORAGE_KEY_STARTPAGE_OVERRIDE: string = 'MEGA_USER_STARTPAGE';

  user: BehaviorSubject<User> = new BehaviorSubject(undefined);

  constructor(private router: Router,
              private httpClient: HttpClient,
              private oAuthService: OAuthService,
              private configService: ConfigService) {

  }

  public loginUser(): void {
    if (sessionStorage.getItem(this.SESSION_STORAGE_KEY)) {
      this.user.next(JSON.parse(sessionStorage.getItem(this.SESSION_STORAGE_KEY)));
      this.navigateToStartpage();
    } else {
      this.httpClient.post<User>(this.configService.getBackendUrl() + '/user/login/', this.oAuthService.getIdToken())
        .subscribe((result) => {
          sessionStorage.setItem(this.SESSION_STORAGE_KEY, JSON.stringify(result));
          this.user.next(result);
          this.navigateToStartpage();
        });
    }
  }

  public logout(): void {
    this.httpClient.post<void>(this.configService.getBackendUrl() + '/user/logout', null).subscribe(() => {
      this.oAuthService.logOut();
      this.configService.logOut();
      this.user.next(undefined);
      sessionStorage.removeItem(this.SESSION_STORAGE_KEY);

      this.router.navigate([configuration.PAGE_URLS.LOGIN]);
    });
  }

  public loggedInWithGoogle(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  public setStartpageOverride(startpage: string): void {
    if (!startpage) {
      sessionStorage.removeItem(this.SESSION_STORAGE_KEY_STARTPAGE_OVERRIDE);
    } else {
      sessionStorage.setItem(this.SESSION_STORAGE_KEY_STARTPAGE_OVERRIDE, startpage);
    }
  }

  public getStartpageOverride(): string {
    return sessionStorage.getItem(this.SESSION_STORAGE_KEY_STARTPAGE_OVERRIDE);
  }

  private navigateToStartpage() {
    let startpage = this.getStartpageOverride() ? this.getStartpageOverride() : configuration.PAGE_URLS.MONTHLY_REPORT;
    this.setStartpageOverride(undefined);
    this.router.navigate([startpage]);
  }
}
