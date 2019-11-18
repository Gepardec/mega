import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {AuthService, GoogleLoginProvider, SocialUser} from "angularx-social-login";
import {Router} from "@angular/router";
import {configuration} from "../../configuration/configuration";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {retry} from "rxjs/operators";
import {MitarbeiterType} from "../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Injectable({
  providedIn: 'root'
})
export class MockAuthenticationService implements OnDestroy {

  private readonly URL: string = environment.backendOrigin;

  private readonly CURRENT_USER: string = 'currentUser';
  private readonly CURRENT_EMPLOYEE: string = 'currentEmployee';
  private readonly HOME_PAGE: string = configuration.PAGES.HOME;
  private readonly LOGIN_PAGE: string = configuration.PAGES.LOGIN;

  private isSignedInWithGoogle: boolean;

  private currentUserSubject: BehaviorSubject<SocialUser>;
  public currentUser: Observable<SocialUser>;

  private currentEmployeeSubject: BehaviorSubject<MitarbeiterType>;
  public currentEmployee: Observable<MitarbeiterType>;

  private authServiceSubscription: Subscription;
  private zepLoginSubscription: Subscription;
  private zepLogoutSubscription: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
  ) {
    let user: SocialUser = JSON.parse(localStorage.getItem(this.CURRENT_USER));
    let employee: MitarbeiterType = JSON.parse(localStorage.getItem(this.CURRENT_EMPLOYEE))
    if (user == null) {
      this.router.navigate([this.LOGIN_PAGE]);
    }
    this.currentUserSubject = new BehaviorSubject<SocialUser>(user);
    this.currentUser = this.currentUserSubject.asObservable();

    this.currentEmployeeSubject = new BehaviorSubject<MitarbeiterType>(employee);
    this.currentEmployee = this.currentEmployeeSubject.asObservable();

    this.authServiceSubscription = this.authService.authState.subscribe((user: SocialUser) => {
      this.isSignedInWithGoogle = user != null;
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
    }

    return user;
  }

  logout() {
    // remove user from local storage to log user out

  }


  signinWithGoogle(): void {
    if (!this.isSignedInWithGoogle) {
      this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(
        () => {
          this.isSignedInWithGoogle = true;
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


  zepLogin(user: SocialUser): Observable<MitarbeiterType> {
    return this.http.post<MitarbeiterType>(this.URL +
      '/user/login/', JSON.stringify(user))
      .pipe(
        retry(1),
      );
  }

  zepLogout(user: SocialUser): Observable<Response> {
    return this.http.post<Response>(this.URL +
      '/user/logout/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }

  isEmployeeAdmin(): boolean {
    return this.currentEmployeeSubject.getValue().rechte === configuration.EMPLOYEE_ROLES.ADMINISTRATOR;
  }

  isEmployeeUser(): boolean {
    return this.currentEmployeeSubject.getValue().rechte === configuration.EMPLOYEE_ROLES.USER;
  }

  isEmployeeController(): boolean {
    return this.currentEmployeeSubject.getValue().rechte === configuration.EMPLOYEE_ROLES.CONTROLLER;
  }

  isEmployeeUserMitZusatzrechten(): boolean {
    return this.currentEmployeeSubject.getValue().rechte === configuration.EMPLOYEE_ROLES.USER_MIT_ZUSATZRECHTEN;
  }

  isEmployeeAdminOrController(): boolean {
    return this.isEmployeeAdmin() || this.isEmployeeController();
  }


}
