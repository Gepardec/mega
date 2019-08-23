import { TestBed } from '@angular/core/testing';

import { MainLayoutService } from './main-layout.service';

describe('MainLayoutService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MainLayoutService = TestBed.get(MainLayoutService);
    expect(service).toBeTruthy();
  });
});
