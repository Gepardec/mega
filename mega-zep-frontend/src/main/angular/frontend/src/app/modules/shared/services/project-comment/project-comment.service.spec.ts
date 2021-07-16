import { TestBed } from '@angular/core/testing';

import { ProjectCommentService } from './project-comment.service';

describe('ProjectCommentService', () => {
  let service: ProjectCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectCommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
