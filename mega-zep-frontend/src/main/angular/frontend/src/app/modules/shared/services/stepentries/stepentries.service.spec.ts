import {TestBed} from '@angular/core/testing';

import {StepentriesService} from './stepentries.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('StepentriesService', () => {
  let service: StepentriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(StepentriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
