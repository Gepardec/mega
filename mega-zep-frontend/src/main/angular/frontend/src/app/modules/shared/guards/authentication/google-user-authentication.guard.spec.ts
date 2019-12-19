import {inject, TestBed} from '@angular/core/testing';

import {GoogleUserAuthenticationGuard} from './google-user-authentication.guard';
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../../app-routing.module";
import {AppModule} from "../../../../app.module";
import {ActivatedRouteSnapshot, UrlSegment} from "@angular/router";
import {configuration} from "../../constants/configuration";

describe('GoogleUserAuthenticationGuard', () => {

  let router: MockRouter;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        AppModule
      ],
      providers: [
        GoogleUserAuthenticationGuard,
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

  function setupUser(): SocialUser {
    let user: SocialUser = new SocialUser();
    user.id = "123456789";
    user.authToken = "987654321";
    user.email = "max.mustermann@gmail.com";
    user.firstName = "Max";
    user.lastName = "Mustermann";

    return user;
  }

  it('should create', inject([GoogleUserAuthenticationGuard], (guard: GoogleUserAuthenticationGuard) => {
    expect(guard).toBeTruthy();
  }));

  it('should not be activated when user is null', inject([GoogleUserAuthenticationGuard], (guard: GoogleUserAuthenticationGuard) => {
    let route: ActivatedRouteSnapshot = setupRoute();
    expect(guard.canActivate(route, null)).toEqual(false);
  }));

  it('should be activated when user is defined', inject([GoogleUserAuthenticationGuard], (guard: GoogleUserAuthenticationGuard) => {
    guard.user = setupUser();
    let route: ActivatedRouteSnapshot = setupRoute();
    expect(guard.canActivate(route, null)).toEqual(true);
  }));
});

class MockRouter {
  navigate(path) {
  }
}
