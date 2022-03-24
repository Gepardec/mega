import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ConfigService} from '../config/config.service';
import {OAuthService} from 'angular-oauth2-oidc';
import {User} from '../../models/User';
import {configuration} from '../../constants/configuration';
import {Router} from '@angular/router';
import {BehaviorSubject} from 'rxjs';
import {LocalStorageService} from "../local-storage/local-storage.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  user: BehaviorSubject<User> = new BehaviorSubject(undefined);

  constructor(private router: Router,
              private httpClient: HttpClient,
              private oAuthService: OAuthService,
              private configService: ConfigService,
              private localStorageService: LocalStorageService) {
  }

  public loginUser(): void {
    this.httpClient.get<User>(this.configService.getBackendUrlWithContext('/user'))
      .subscribe((result) => {
        this.user.next(result);
        this.navigateToStartpage();
      });
  }

  public logout(): void {
    this.invalidateUser();
    this.router.navigate([configuration.PAGE_URLS.LOGIN]);
  }

  public logoutWithoutRedirect(): void {
    this.invalidateUser();
  }

  invalidateUser() {
    this.oAuthService.logOut();
    this.configService.logOut();
    this.user.next(undefined);
  }

  public loggedInWithGoogle(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  public setStartpageOverride(startpage: string): void {
    !startpage ? this.localStorageService.removeUserStartPage() : this.localStorageService.saveUserStartPage(startpage);
  }

  public getStartpageOverride(): string {
    return this.localStorageService.getUserStartPage();
  }

  private navigateToStartpage() {
    const startpage = this.getStartpageOverride() ? this.getStartpageOverride() : configuration.PAGE_URLS.MONTHLY_REPORT;
    this.setStartpageOverride(undefined);
    this.router.navigate([startpage]);
  }
}
