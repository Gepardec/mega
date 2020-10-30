import { TestBed } from '@angular/core/testing';

import { StepentriesService } from './stepentries.service';

describe('StepentriesService', () => {
  let service: StepentriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StepentriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
