import {TestBed} from '@angular/core/testing';

import {UtilService} from './util.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('UtilService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ]
  }));

  it('should be created', () => {
    const service: UtilService = TestBed.get(UtilService);
    expect(service).toBeTruthy();
  });
});
