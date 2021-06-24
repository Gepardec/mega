import {TestBed} from '@angular/core/testing';

import {ProjectEntriesService} from './project-entries.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ProjectEntriesService', () => {
  let service: ProjectEntriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ProjectEntriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
