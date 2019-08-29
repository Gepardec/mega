import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {AuthService, GoogleLoginProvider, SocialUser} from "angularx-social-login";
import {Router} from "@angular/router";
import {configuration} from "../../configuration/configuration";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, retry} from "rxjs/operators";
import {ErrorHandleService} from "../main-pages/error-handle.service";
import * as HttpStatus from 'http-status-codes'

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly URL: string = configuration.BASEURL;

  private readonly CURRENT_USER: string = 'currentUser';
  private readonly HOME_PAGE: string = configuration.PAGES.HOME;
  private readonly LOGIN_PAGE: string = configuration.PAGES.LOGIN;

  private currentUserSubject: BehaviorSubject<SocialUser>;
  public currentUser: Observable<SocialUser>;

  // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient,
    private errorHandlService: ErrorHandleService
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
      this.zepLogin(user).subscribe(
        (response: Response) => {
          if (response.status === HttpStatus.FORBIDDEN) {
            this.logout();
            this.router.navigate([this.LOGIN_PAGE]);
          }
        }
      );
    }

    return user;
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem(this.CURRENT_USER);
    this.currentUserSubject.next(null);
    this.zepLogout(this.currentUserValue).subscribe(
      (response: Response) => {
        this.router.navigate([this.LOGIN_PAGE]);
      }
    );

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


  zepLogin(user: SocialUser): Observable<Response> {
    return this.http.post<Response>(this.URL +
      '/user/login/', JSON.stringify(user), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandlService.errorHandl)
      );
  }

  zepLogout(user: SocialUser): Observable<Response> {
    return this.http.post<Response>(this.URL +
      '/user/logout/', JSON.stringify(user), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandlService.errorHandl)
      );
  }


}
