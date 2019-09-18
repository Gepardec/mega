import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SidebarComponent} from './sidebar.component';
import {NgZone, NO_ERRORS_SCHEMA} from "@angular/core";
import {RouterTestingModule} from "@angular/router/testing";
import {MainLayoutModule} from "../../main-layout/main-layout/main-layout.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {routes} from "../../../app-routing.module";
import {AppModule} from "../../../app.module";
import {Mock} from "protractor/built/driverProviders";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";

describe('SidebarComponent', () => {
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
        {provide: AuthService, useClass: MockAuthService}],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<SidebarComponent> = TestBed.createComponent(SidebarComponent);
    const app: SidebarComponent = fixture.debugElement.componentInstance;

    spyOn(app, "isEmployeeAdminOrController").and.returnValue(true);
    fixture.detectChanges();

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should toggle sidenav', () => {
    const {fixture, app} = setup();

    expect(app.sidenav.toggle).toBeDefined();
  });
});
