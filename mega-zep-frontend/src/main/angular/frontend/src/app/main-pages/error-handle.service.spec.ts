import { TestBed } from '@angular/core/testing';

import { ErrorHandleService } from './error-handle.service';

describe('ErrorHandleService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ErrorHandleService = TestBed.get(ErrorHandleService);
    expect(service).toBeTruthy();
  });
});
