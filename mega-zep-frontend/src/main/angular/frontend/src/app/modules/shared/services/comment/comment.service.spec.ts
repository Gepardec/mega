import {TestBed} from '@angular/core/testing';

import {CommentService} from './comment.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('CommentService', () => {
  let service: CommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(CommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
