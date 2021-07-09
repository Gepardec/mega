import {TestBed} from '@angular/core/testing';

import {EnterpriseEntriesService} from './enterprise-entries.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('EnterpriseEntriesService', () => {
  let service: EnterpriseEntriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(EnterpriseEntriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
