import { TestBed, async, inject } from '@angular/core/testing';

import { GoogleUserAuthenticationGuard } from './google-user-authentication.guard';
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../app-routing.module";
import {MainLayoutModule} from "../main-layout/main-layout/main-layout.module";
import {AppModule} from "../../app.module";
import {AuthenticationService} from "../../signin/authentication.service";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../signin/MockAuthService";
import {MockAuthenticationService} from "../../signin/MockAuthenticationService";
import {NgZone} from "@angular/core";

describe('GoogleUserAuthenticationGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        MainLayoutModule,
        AppModule
      ],
      providers: [
        GoogleUserAuthenticationGuard,
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    });
  });

  it('should create', inject([GoogleUserAuthenticationGuard], (guard: GoogleUserAuthenticationGuard) => {
    expect(guard).toBeTruthy();
  }));
});
