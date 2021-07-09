import { TestBed } from '@angular/core/testing';

import { EnterpriseEntriesService } from './enterprise-entries.service';

describe('EnterpriseEntriesService', () => {
  let service: EnterpriseEntriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnterpriseEntriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
