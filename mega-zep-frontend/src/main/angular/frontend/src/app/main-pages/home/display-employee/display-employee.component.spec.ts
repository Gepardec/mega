import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DisplayEmployeeComponent} from './display-employee.component';
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../app-routing.module";
import {AppModule} from "../../../app.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";
import {NgZone} from "@angular/core";

describe('DisplayEmployeeComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AppModule,
        RouterTestingModule.withRoutes(routes)
      ],
      declarations: [DisplayEmployeeComponent],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DisplayEmployeeComponent> = TestBed.createComponent(DisplayEmployeeComponent);
    const app: DisplayEmployeeComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
