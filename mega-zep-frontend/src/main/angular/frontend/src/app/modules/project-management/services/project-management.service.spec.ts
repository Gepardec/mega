import { TestBed } from '@angular/core/testing';

import { ProjectManagementService } from './project-management.service';

describe('ProjectManagementService', () => {
  let service: ProjectManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
