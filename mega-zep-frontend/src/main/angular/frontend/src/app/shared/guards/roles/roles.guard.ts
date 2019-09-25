import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthenticationService} from "../../../signin/authentication.service";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Injectable({
  providedIn: 'root'
})
export class RolesGuard implements CanActivate {

  private employee: MitarbeiterType;

  constructor(
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentEmployee.subscribe(
      (employee: MitarbeiterType) => this.employee = employee
    );
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.employee == null) {
      return false;
    }

    let roles = route.data.roles as Array<number>;
    console.log(roles);
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
