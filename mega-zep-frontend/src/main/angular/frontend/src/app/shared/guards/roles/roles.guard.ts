import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from "../../../signin/authentication.service";
import {Employee} from "../../../modules/shared/models/Employee/Employee";

@Injectable({
  providedIn: 'root'
})
export class RolesGuard implements CanActivate {

  employee: Employee;

  constructor(
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentEmployee.subscribe(
      (employee: Employee) => this.employee = employee
    );
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.employee == null) {
      return false;
    }

    let roles = route.data.roles as Array<number>;
    // allow all roles when roles array is empty
    if (roles.length === 0) {
      return true;
    }

    for (let role of roles) {
      if (role === this.employee.rechte) {
        return true;
      }
    }
    return false;
  }
}
