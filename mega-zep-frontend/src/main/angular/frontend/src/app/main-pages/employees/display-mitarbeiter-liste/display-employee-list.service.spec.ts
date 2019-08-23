import { TestBed } from '@angular/core/testing';

import { DisplayEmployeeListService } from './display-employee-list.service';

describe('DisplayEmployeeListService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DisplayEmployeeListService = TestBed.get(DisplayEmployeeListService);
    expect(service).toBeTruthy();
  });
});
