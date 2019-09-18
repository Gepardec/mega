import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GoogleSigninComponent} from './google-signin.component';
import {AuthenticationService} from "../authentication.service";
import {AuthService} from "angularx-social-login";
import {RouterTestingModule} from "@angular/router/testing";
import {NgZone, NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MainLayoutModule} from "../../shared/main-layout/main-layout/main-layout.module";
import {routes} from "../../app-routing.module";
import {APP_BASE_HREF} from "@angular/common";
import {MockAuthService} from "../MockAuthService";
import {MockAuthenticationService} from "../MockAuthenticationService";

describe('GoogleSigninComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes), HttpClientTestingModule, MainLayoutModule],
      declarations: [GoogleSigninComponent],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
        {provide: APP_BASE_HREF, useValue: '/'},
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<GoogleSigninComponent> = TestBed.createComponent(GoogleSigninComponent);
    const app: GoogleSigninComponent = fixture.debugElement.componentInstance;

    fixture.detectChanges();
    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
