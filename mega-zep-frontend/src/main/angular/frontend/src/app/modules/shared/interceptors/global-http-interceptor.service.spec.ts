import { TestBed } from '@angular/core/testing';

import { GlobalHttpInterceptorService } from './global-http-interceptor.service';

describe('GlobalHttpInterceptorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GlobalHttpInterceptorService = TestBed.get(GlobalHttpInterceptorService);
    expect(service).toBeTruthy();
  });
});
