import {Injectable, OnDestroy} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {SocialUser} from "angularx-social-login";
import {ZepSigninService} from "../../services/signin/zep-signin.service";
import {configuration} from "../../constants/configuration";

@Injectable({
  providedIn: 'root'
})
export class GoogleUserAuthenticationGuard implements CanActivate, OnDestroy {

  readonly loginPage: string = configuration.PAGE_URLS.LOGIN;

  user: SocialUser;
  currentUserSubscription: Subscription;

  constructor(
    private authenticationService: ZepSigninService,
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
