import {TestBed} from '@angular/core/testing';

import {InfoService} from './info.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('InfoService', () => {
  let service: InfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(InfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
