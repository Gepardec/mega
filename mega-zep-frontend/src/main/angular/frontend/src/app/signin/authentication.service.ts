import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {AuthService, GoogleLoginProvider, SocialUser} from "angularx-social-login";
import {Router} from "@angular/router";
import {configuration} from "../../configuration/configuration";
import {HttpClient} from "@angular/common/http";
import {retry} from "rxjs/operators";
import * as HttpStatus from 'http-status-codes';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService implements OnDestroy {

  private readonly URL: string = configuration.BASEURL;

  private readonly CURRENT_USER: string = 'currentUser';
  private readonly HOME_PAGE: string = configuration.PAGES.HOME;
  private readonly LOGIN_PAGE: string = configuration.PAGES.LOGIN;

  private isSignedInWithGoogle = false;

  private currentUserSubject: BehaviorSubject<SocialUser>;
  public currentUser: Observable<SocialUser>;

  private authServiceSubscription: Subscription;
  private zepLoginSubscription: Subscription;
  private zepLogoutSubscription: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
  ) {
    this.currentUserSubject = new BehaviorSubject<SocialUser>(JSON.parse(localStorage.getItem(this.CURRENT_USER)));
    this.currentUser = this.currentUserSubject.asObservable();

    this.authServiceSubscription = this.authService.authState.subscribe((user) => {
      if (user != null) {
        this.login(user);
      } else {
        this.logout();
      }
    });
  }

  ngOnDestroy(): void {
    this.authServiceSubscription && this.authServiceSubscription.unsubscribe();
    this.zepLoginSubscription && this.zepLoginSubscription.unsubscribe();
    this.zepLogoutSubscription && this.zepLogoutSubscription.unsubscribe();
  }

  public get currentUserValue(): SocialUser {
    return this.currentUserSubject.value;
  }

  login(user: SocialUser) {
    if (user && user.authToken) {
      // store user details and token in local storage to keep user logged in between page refreshes
      localStorage.setItem(this.CURRENT_USER, JSON.stringify(user));
      this.currentUserSubject.next(user);
      this.zepLoginSubscription = this.zepLogin(user).subscribe(
        (response: Response) => {
          if (response.status === HttpStatus.FORBIDDEN) {
            this.router.navigate([this.LOGIN_PAGE]);
            this.logout();
          }
        }
      );
    }

    return user;
  }

  logout() {
    // remove user from local storage to log user out
    this.zepLogoutSubscription = this.zepLogout(this.currentUserValue).subscribe(
      (response: Response) => {
        localStorage.removeItem(this.CURRENT_USER);
        this.currentUserSubject.next(null);
        this.signOut();
      }
    );

  }


  signinWithGoogle(): void {
    if (!this.isSignedInWithGoogle) {
      this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(
        () => {
          this.isSignedInWithGoogle = true;
          this.router.navigate([this.HOME_PAGE]);
        });
    }
  }

  signOut(): void {
    if (this.isSignedInWithGoogle) {
      this.authService.signOut().then(
        () => {
          this.isSignedInWithGoogle = false;
          this.router.navigate([this.LOGIN_PAGE]);
        });
    }
  }


  zepLogin(user: SocialUser): Observable<Response> {
    return this.http.post<Response>(this.URL +
      '/user/login/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }

  zepLogout(user: SocialUser): Observable<Response> {
    return this.http.post<Response>(this.URL +
      '/user/logout/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }


}
