import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {UserActionsComponent} from './user-actions.component';
import {AuthenticationService} from "../../../../signin/authentication.service";
import {AuthService, SocialUser} from "angularx-social-login";
import {MockAuthService} from "../../../../signin/MockAuthService";
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../../app-routing.module";
import {AppModule} from "../../../../app.module";
import {MainLayoutModule} from "../../../main-layout/main-layout/main-layout.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MockAuthenticationService} from "../../../../signin/MockAuthenticationService";
import {NgZone} from "@angular/core";

describe('UserActionsComponent', () => {

  let authenticationService: MockAuthenticationService = null;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        AppModule,
        MainLayoutModule,
        HttpClientTestingModule,
        BrowserAnimationsModule],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService}
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<UserActionsComponent> = TestBed.createComponent(UserActionsComponent);
    const app: UserActionsComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
