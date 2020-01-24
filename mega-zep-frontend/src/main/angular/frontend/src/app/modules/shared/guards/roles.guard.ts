import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../models/User';
import { UserService } from '../services/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class RolesGuard implements CanActivate {

  constructor(private userService: UserService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const user: User = this.userService.user.value;

    if (!user) {
      return false;
    }

    const roles = route.data.roles as Array<string>;
    // allow all roles when roles array is empty
    if (roles.length === 0) {
      return true;
    }

    for (const role of roles) {
      if (role === user.role) {
        return true;
      }
    }
    return false;
  }
}
