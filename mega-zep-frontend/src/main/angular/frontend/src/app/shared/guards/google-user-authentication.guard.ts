import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../signin/authentication.service";

@Injectable({
  providedIn: 'root'
})
export class GoogleUserAuthenticationGuard implements CanActivate {

  private user: SocialUser;

  constructor(
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
    });
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean |
    UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.user != null;
  }

}
