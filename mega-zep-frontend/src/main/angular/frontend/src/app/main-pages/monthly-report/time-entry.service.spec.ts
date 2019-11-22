import { TestBed } from '@angular/core/testing';

import { TimeEntryService } from './time-entry.service';

describe('TimeEntryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TimeEntryService = TestBed.get(TimeEntryService);
    expect(service).toBeTruthy();
  });
});
