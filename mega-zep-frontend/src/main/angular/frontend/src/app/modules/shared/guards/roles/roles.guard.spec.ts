import {inject, TestBed} from '@angular/core/testing';

import {RolesGuard} from './roles.guard';
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../../app-routing.module";
import {AppModule} from "../../../../app.module";
import {AuthenticationService} from "../../../../signin/zep-signin/authentication.service";
import {MockAuthenticationService} from "../../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../../signin/MockAuthService";
import {ActivatedRouteSnapshot, UrlSegment} from "@angular/router";
import {configuration} from "../../../../../configuration/configuration";
import {Employee} from "../../models/Employee/Employee";

describe('RolesGuard', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        AppModule
      ],
      providers: [
        RolesGuard,
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    });
  });

  function setupRoute(): ActivatedRouteSnapshot {
    let route = new ActivatedRouteSnapshot();
    route.url = new Array<UrlSegment>();
    route.url.push(new UrlSegment("/employees", null));
    route.data = {roles: [configuration.EMPLOYEE_ROLES.ADMINISTRATOR, configuration.EMPLOYEE_ROLES.CONTROLLER]};
    return route;
  }

  function setupUser(rights: number): Employee {
    let employee: Employee = new Employee();
    employee.vorname = "Max";
    employee.nachname = "Mustermann";
    employee.freigabedatum = "2019-01-01";
    employee.rechte = rights;

    return employee;
  }

  it('should create', inject([RolesGuard], (guard: RolesGuard) => {
    expect(guard).toBeTruthy();
  }));

  it('should not be activated by user equals null', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    expect(guard.canActivate(route, null)).toEqual(false);
  }));

  it('should not be activated by user with no rights', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    let employee = setupUser(configuration.EMPLOYEE_ROLES.USER);
    guard.employee = employee;
    expect(guard.canActivate(route, null)).toEqual(false);
  }));

  it('should be activated by user with rights', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    let employee = setupUser(configuration.EMPLOYEE_ROLES.ADMINISTRATOR);
    guard.employee = employee;
    expect(guard.canActivate(route, null)).toEqual(true);
  }));
});


