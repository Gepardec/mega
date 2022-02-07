import {TestBed, waitForAsync} from '@angular/core/testing';

import {AuthorizationHeaderInterceptor} from './authorization-header.interceptor';
import {OAuthStorage} from 'angular-oauth2-oidc';
import {ConfigService} from '../services/config/config.service';

describe('AuthorizationHeaderInterceptor', () => {

  beforeEach(waitForAsync(() => TestBed.configureTestingModule({
      providers: [
        AuthorizationHeaderInterceptor,
        OAuthStorage,
        {provide: ConfigService, useClass: ConfigServiceMock}
      ]
    }).compileComponents()
  ));

  it('should be created', () => {
    const interceptor: AuthorizationHeaderInterceptor = TestBed.inject(AuthorizationHeaderInterceptor);
    expect(interceptor).toBeTruthy();
  });

  class ConfigServiceMock {
  }
});
