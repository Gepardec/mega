import { TestBed } from '@angular/core/testing';

import { AuthorizationHeaderInterceptor } from './authorization-header.interceptor';
import { OAuthStorage } from 'angular-oauth2-oidc';

describe('AuthorizationHeaderInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AuthorizationHeaderInterceptor,
      OAuthStorage
    ]
  }));

  it('should be created', () => {
    const interceptor: AuthorizationHeaderInterceptor = TestBed.inject(AuthorizationHeaderInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
