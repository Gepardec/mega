import {TestBed} from '@angular/core/testing';

import {UserService} from './user.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {OAuthService} from 'angular-oauth2-oidc';

describe('UserService', () => {

  class OAuthServiceMock {

  }

  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      RouterTestingModule,
      HttpClientTestingModule
    ],
    providers: [
      {
        provide: OAuthService, useClass: OAuthServiceMock
      }
    ],
  }));

  it('should be created', () => {
    const service: UserService = TestBed.get(UserService);
    expect(service).toBeTruthy();
  });
});
