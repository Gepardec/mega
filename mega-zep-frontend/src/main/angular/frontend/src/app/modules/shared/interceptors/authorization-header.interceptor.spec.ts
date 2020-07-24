import { TestBed } from '@angular/core/testing';

import { AuthorizationHeaderInterceptor } from './authorization-header.interceptor';

describe('AuthorizationHeaderInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AuthorizationHeaderInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: AuthorizationHeaderInterceptor = TestBed.inject(AuthorizationHeaderInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
