import {inject, TestBed} from '@angular/core/testing';

import {RolesGuard} from './roles.guard';
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../app-routing.module";
import {AppModule} from "../../../app.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";
import {NgZone} from "@angular/core";

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

  it('should ...', inject([RolesGuard], (guard: RolesGuard) => {
    expect(guard).toBeTruthy();
  }));
});
