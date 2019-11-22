import {Injectable, OnDestroy} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../signin/authentication.service";
import {configuration} from "../../../configuration/configuration";

@Injectable({
  providedIn: 'root'
})
export class GoogleUserAuthenticationGuard implements CanActivate, OnDestroy {

  readonly loginPage: string = configuration.PAGES.filter(p => p.pageName == 'LOGIN')[0].pageUrl;

  user: SocialUser;
  currentUserSubscription: Subscription;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean |
    UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.user == null) {
      this.router.navigate([this.loginPage]);
      return false;
    } else {
      return true;
    }
  }

}
