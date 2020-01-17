import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {ZepSigninService} from "../../services/signin/zep-signin.service";
import {Employee} from "../../models/Employee/Employee";
import {User} from "../../models/User/User";

@Injectable({
  providedIn: 'root'
})
export class RolesGuard implements CanActivate {

  employee: User;

  constructor(
    private authenticationService: ZepSigninService
  ) {
    this.authenticationService.currentEmployee.subscribe(
      (employee: User) => this.employee = employee
    );
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.employee == null) {
      return false;
    }

    let roles = route.data.roles as Array<string>;
    // allow all roles when roles array is empty
    if (roles.length === 0) {
      return true;
    }

    for (let role of roles) {
      if (role === this.employee.role) {
        return true;
      }
    }
    return false;
  }
}
