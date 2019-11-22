import { TestBed } from '@angular/core/testing';

import { DisplayTimeEntryService } from './display-time-entry.service';

describe('DisplayTimeEntryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DisplayTimeEntryService = TestBed.get(DisplayTimeEntryService);
    expect(service).toBeTruthy();
  });
});
