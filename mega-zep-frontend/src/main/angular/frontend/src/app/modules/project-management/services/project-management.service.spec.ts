import {TestBed} from '@angular/core/testing';

import {ProjectManagementService} from './project-management.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ProjectManagementService', () => {
  let service: ProjectManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(ProjectManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
