import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {UserActionsComponent} from './user-actions.component';
import {AuthenticationService} from "../../../../../signin/zep-signin/authentication.service";
import {AuthService, SocialUser} from "angularx-social-login";
import {MockAuthService} from "../../../../../signin/MockAuthService";
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../../../app-routing.module";
import {AppModule} from "../../../../../app.module";
import {MainLayoutModule} from "../../../main-layout/main-layout.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MockAuthenticationService} from "../../../../../signin/MockAuthenticationService";
import {By} from "@angular/platform-browser";
import {DebugElement} from "@angular/core";
import {MatButton, MatMenu, MatMenuTrigger} from "@angular/material";

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

    fixture.detectChanges();

    return {fixture, app};
  }

  function setupUser(): SocialUser {
    let user: SocialUser = new SocialUser();
    user.id = "123456789";
    user.authToken = "987654321";
    user.email = "max.mustermann@gmail.com";
    user.firstName = "Max";
    user.lastName = "Mustermann";
    user.photoUrl = "https://taz.de/picture/2588561/948/Gepard_imago_Anka_Agency_International_6h.jpeg";

    return user;
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should display first name and last name of user', () => {
    const {fixture, app} = setup();
    let user = setupUser();
    app.user = user;
    fixture.detectChanges();

    expect(fixture.debugElement.nativeElement.querySelector('#userBtn').textContent)
      .toEqual(user.firstName + " " + user.lastName + " keyboard_arrow_down");
  });

  it('should display photo url', () => {
    const {fixture, app} = setup();
    let user = setupUser();
    app.user = user;
    let img: DebugElement = fixture.debugElement.query(By.css(".avatar"));
    fixture.detectChanges();

    expect(img.nativeElement.src).toEqual(user.photoUrl);
  });
});
