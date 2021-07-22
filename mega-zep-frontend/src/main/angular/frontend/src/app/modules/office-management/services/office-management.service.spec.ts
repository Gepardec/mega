import {TestBed} from '@angular/core/testing';

import {OfficeManagementService} from './office-management.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('OfficeManagementService', () => {
  let service: OfficeManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(OfficeManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
