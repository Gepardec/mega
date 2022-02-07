import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {OAuthService} from 'angular-oauth2-oidc';
import {RouterTestingModule} from '@angular/router/testing';
import {UserService} from '../../services/user/user.service';
import {TranslateModule} from '@ngx-translate/core';

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authService: OAuthService;
  let userService: UserService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        {provide: OAuthService, useClass: OAuthServiceMock},
        {provide: UserService, useClass: UserServiceMock}
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;

      authService = TestBed.inject(OAuthService);
      userService = TestBed.inject(UserService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#login - should call authService.initLoginFlow()', () => {
    spyOn(authService, 'initLoginFlow').and.stub();

    component.login();

    expect(authService.initLoginFlow).toHaveBeenCalled();
  });

  it('#loggedIn - should call userService.loggedInWithGoogle and return true', () => {
    spyOn(userService, 'loggedInWithGoogle').and.returnValue(true);

    const loggedIn = component.loggedIn();

    expect(userService.loggedInWithGoogle).toHaveBeenCalled();
    expect(loggedIn).toBeTrue();
  });

  class OAuthServiceMock {
    initLoginFlow() {
    }
  }

  class UserServiceMock {

    loggedInWithGoogle(): boolean {
      return true;
    }
  }
});
