import {TestBed} from '@angular/core/testing';

import {ProjectCommentService} from './project-comment.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ProjectCommentService', () => {
  let service: ProjectCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ProjectCommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
