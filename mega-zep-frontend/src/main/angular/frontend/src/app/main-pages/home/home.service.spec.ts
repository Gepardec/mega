import { TestBed } from '@angular/core/testing';

import { HomeService } from './home.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('HomeService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule]
  }));

  it('should be created', () => {
    const service: HomeService = TestBed.get(HomeService);
    expect(service).toBeTruthy();
  });
});
