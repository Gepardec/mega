import {Injectable, OnDestroy} from '@angular/core';
import {configuration} from "../../../../../configuration/configuration";
import {BehaviorSubject, Observable, Subscription} from "rxjs";
import {AuthService, GoogleLoginProvider, SocialUser} from "angularx-social-login";
import {Employee} from "../../models/Employee/Employee";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {retry} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ZepSigninService implements OnDestroy {

  private readonly URL: string = configuration.BASEURL;

  private readonly CURRENT_USER: string = 'currentUser';
  private readonly CURRENT_EMPLOYEE: string = 'currentEmployee';
  private readonly MONTHLY_REPORT_PAGE: string = configuration.PAGE_URLS.MONTHLY_REPORT;
  private readonly LOGIN_PAGE: string = configuration.PAGE_URLS.LOGIN;

  private isSignedInWithGoogle: boolean;

  private currentUserSubject: BehaviorSubject<SocialUser>;
  public currentUser: Observable<SocialUser>;

  private currentEmployeeSubject: BehaviorSubject<Employee>;
  public currentEmployee: Observable<Employee>;

  private authServiceSubscription: Subscription;
  private zepLoginSubscription: Subscription;
  private zepLogoutSubscription: Subscription;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,
  ) {
    let user: SocialUser = JSON.parse(localStorage.getItem(this.CURRENT_USER));
    let employee: Employee = JSON.parse(localStorage.getItem(this.CURRENT_EMPLOYEE))
    if (user == null) {
      this.router.navigate([this.LOGIN_PAGE]);
    }
    this.currentUserSubject = new BehaviorSubject<SocialUser>(user);
    this.currentUser = this.currentUserSubject.asObservable();

    this.currentEmployeeSubject = new BehaviorSubject<Employee>(employee);
    this.currentEmployee = this.currentEmployeeSubject.asObservable();

    this.authServiceSubscription = this.authService.authState.subscribe((user: SocialUser) => {
      this.isSignedInWithGoogle = user != null;
      user != null ? this.login(user) : this.logout();
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
        (employee: Employee) => {
          this.currentEmployeeSubject = new BehaviorSubject<Employee>(employee);
          this.currentEmployee = this.currentEmployeeSubject.asObservable();
          localStorage.setItem(this.CURRENT_EMPLOYEE, JSON.stringify(employee));
          this.router.navigate([this.MONTHLY_REPORT_PAGE]);
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
        localStorage.removeItem(this.CURRENT_EMPLOYEE);
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


  zepLogin(user: SocialUser): Observable<Employee> {
    return this.http.post<Employee>(this.URL +
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
