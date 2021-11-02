import {TestBed} from '@angular/core/testing';

import {AuthorizationHeaderInterceptor} from './authorization-header.interceptor';
import {OAuthStorage} from 'angular-oauth2-oidc';
import {ConfigService} from '../services/config/config.service';

describe('AuthorizationHeaderInterceptor', () => {

  class ConfigServiceMock {

  }

  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AuthorizationHeaderInterceptor,
      OAuthStorage,
      {
        provide: ConfigService, useClass: ConfigServiceMock
      }
    ]
  }));

  it('should be created', () => {
    const interceptor: AuthorizationHeaderInterceptor = TestBed.inject(AuthorizationHeaderInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
