import {TestBed} from '@angular/core/testing';

import {DisplayEmployeesService} from './display-employees.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DisplayEmployeeListService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should create', () => {
    const service: DisplayEmployeesService = TestBed.get(DisplayEmployeesService);
    expect(service).toBeTruthy();
  });
});
