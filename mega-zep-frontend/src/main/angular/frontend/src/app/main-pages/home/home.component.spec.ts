import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HomeComponent} from './home.component';
import {HomePagesModule} from "./home.module";
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../app-routing.module";
import {AppModule} from "../../app.module";
import {AuthenticationService} from "../../signin/authentication.service";
import {MockAuthenticationService} from "../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../signin/MockAuthService";
import {NgZone} from "@angular/core";

describe('HomeComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HomePagesModule,
        RouterTestingModule.withRoutes(routes),
        AppModule
      ],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<HomeComponent> = TestBed.createComponent(HomeComponent);
    const app: HomeComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
