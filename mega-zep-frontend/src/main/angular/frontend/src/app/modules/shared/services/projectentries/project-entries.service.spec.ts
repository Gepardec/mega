import { TestBed } from '@angular/core/testing';

import { ProjectEntriesService } from './project-entries.service';

describe('ProjectEntriesService', () => {
  let service: ProjectEntriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectEntriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
