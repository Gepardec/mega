import { TestBed } from '@angular/core/testing';

import { ZepSigninService } from './zep-signin.service';

describe('ZepSigninService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ZepSigninService = TestBed.get(ZepSigninService);
    expect(service).toBeTruthy();
  });
});
