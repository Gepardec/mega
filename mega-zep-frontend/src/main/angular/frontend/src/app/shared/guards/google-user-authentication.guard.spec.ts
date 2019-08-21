import { TestBed, async, inject } from '@angular/core/testing';

import { GoogleUserAuthenticationGuard } from './google-user-authentication.guard';

describe('GoogleUserAuthenticationGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GoogleUserAuthenticationGuard]
    });
  });

  it('should ...', inject([GoogleUserAuthenticationGuard], (guard: GoogleUserAuthenticationGuard) => {
    expect(guard).toBeTruthy();
  }));
});
