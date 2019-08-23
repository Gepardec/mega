import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {AuthService, GoogleLoginProvider, SocialUser} from "angularx-social-login";
import {Router} from "@angular/router";
import {configuration} from "../../configuration/configuration";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly CURRENT_USER: string = 'currentUser';
  private readonly HOME_PAGE: string = configuration.PAGES.HOME;
  private readonly LOGIN_PAGE: string = configuration.PAGES.LOGIN;

  private currentUserSubject: BehaviorSubject<SocialUser>;
  public currentUser: Observable<SocialUser>;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUserSubject = new BehaviorSubject<SocialUser>(JSON.parse(localStorage.getItem(this.CURRENT_USER)));
    this.currentUser = this.currentUserSubject.asObservable();

    this.authService.authState.subscribe((user) => {
      if (user != null) {
        this.login(user);
      } else {
        this.logout();
      }
    });
  }

  public get currentUserValue(): SocialUser {
    return this.currentUserSubject.value;
  }

  login(user: SocialUser) {
    if (user && user.authToken) {
      // store user details and token in local storage to keep user logged in between page refreshes
      localStorage.setItem(this.CURRENT_USER, JSON.stringify(user));
      this.currentUserSubject.next(user);
    }

    return user;
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem(this.CURRENT_USER);
    this.currentUserSubject.next(null);
  }



  signinWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(
      () => this.router.navigate([this.HOME_PAGE])
    );
  }

  signOut(): void {
    this.authService.signOut().then(
      () => this.router.navigate([this.LOGIN_PAGE])
    );
  }


}
