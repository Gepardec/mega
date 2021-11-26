import {fakeAsync, TestBed} from '@angular/core/testing';

import {UserService} from './user.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {OAuthService} from 'angular-oauth2-oidc';
import {ConfigService} from "../config/config.service";
import {Role} from "../../models/Role";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

describe('UserService', () => {

  let userService: UserService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;
  let httpClient: HttpClient;
  let router: Router;
  let oAuthService: OAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ],
      providers: [
        {
          provide: OAuthService, useClass: OAuthServiceMock
        }
      ],
    });

    userService = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
    httpClient = TestBed.inject(HttpClient);
    router = TestBed.inject(Router);
    oAuthService = TestBed.inject(OAuthService);
  });

  it('#should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('#loginUser - should call user.next()', fakeAsync(() => {
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));
    spyOn(userService.user, 'next').and.callThrough();

    userService.loginUser();

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/user'));
    testRequest.flush(UserMock.get());

    expect(userService.user.next).toHaveBeenCalledWith(UserMock.get());
  }));

  it('#logout - should be logged out', () => {
    spyOn(oAuthService, 'logOut').and.stub();
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));
    spyOn(userService.user, 'next').and.callThrough();

    userService.logout();

    expect(userService.user.next).toHaveBeenCalledWith(undefined);
  });

  it('#logoutWithoutRedirect - should be logged out without redirect', () => {
    spyOn(oAuthService, 'logOut').and.stub();
    spyOn(userService.user, 'next').and.callThrough();

    userService.logoutWithoutRedirect();

    expect(userService.user.next).toHaveBeenCalledWith(undefined);
  });

  it('#loggedInWithGoogle - should call oAuthService.hasValidAccessToken()', () => {
    spyOn(oAuthService, 'hasValidAccessToken').and.returnValue(true);

    userService.loggedInWithGoogle();

    expect(oAuthService.hasValidAccessToken).toHaveBeenCalledWith();
  });

  it('#setStartpageOverride - should set item in sessionStorage', () => {
    userService.setStartpageOverride(UserMock.startPage);

    expect(sessionStorage.getItem(UserMock.sessionStorageKeyStartpageOverride)).toEqual(UserMock.startPage);
  });

  it('#setStartpageOverride - should remove item in sessionStorage', () => {
    userService.setStartpageOverride(UserMock.startPage);
    expect(sessionStorage.getItem(UserMock.sessionStorageKeyStartpageOverride)).toEqual(UserMock.startPage);

    userService.setStartpageOverride(undefined);
    expect(sessionStorage.getItem(UserMock.sessionStorageKeyStartpageOverride)).not.toBeTruthy();
  });

  it('#getStartpageOverride - should get item in sessionStorage', () => {
    userService.setStartpageOverride(UserMock.startPage);
    expect(userService.getStartpageOverride()).toEqual(UserMock.startPage);
  });

  class OAuthServiceMock {

    public logOut(): void {
    }

    public hasValidAccessToken(): boolean {
      return true;
    }

  }

  class UserMock {

    static startPage: string = 'home';

    static sessionStorageKeyStartpageOverride: string = 'MEGA_USER_STARTPAGE';

    static get() {
      return {
        userId: '011-max',
        email: 'maxm@gepardec.com',
        firstname: 'max',
        lastname: 'muster',
        roles: [Role.EMPLOYEE]
      }
    }

  }
});
