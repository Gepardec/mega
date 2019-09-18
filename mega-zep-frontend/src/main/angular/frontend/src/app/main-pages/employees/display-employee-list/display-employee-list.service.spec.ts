import {TestBed} from '@angular/core/testing';

import {DisplayEmployeeListService} from './display-employee-list.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DisplayEmployeeListService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: DisplayEmployeeListService = TestBed.get(DisplayEmployeeListService);
    expect(service).toBeTruthy();
  });
});
