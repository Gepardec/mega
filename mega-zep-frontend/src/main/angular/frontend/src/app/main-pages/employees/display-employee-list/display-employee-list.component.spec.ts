import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DisplayEmployeeListComponent} from './display-employee-list.component';
import {AngularMaterialModule} from "../../../material-module";
import {EmployeesPagesModule} from "../home/home.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppModule} from "../../../app.module";
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../app-routing.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";
import {NgZone} from "@angular/core";

describe('DisplayEmployeeListComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AngularMaterialModule,
        EmployeesPagesModule,
        HttpClientTestingModule,
        AppModule,
        RouterTestingModule.withRoutes(routes)
      ],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DisplayEmployeeListComponent> = TestBed.createComponent(DisplayEmployeeListComponent);
    const app: DisplayEmployeeListComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }


  it('should create', () => {
    const {app} = setup();
    expect(app).toBeTruthy();
  });


});
