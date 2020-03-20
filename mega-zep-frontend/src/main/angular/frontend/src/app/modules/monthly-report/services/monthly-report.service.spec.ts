import { TestBed } from '@angular/core/testing';

import { MonthlyReportService } from './monthly-report.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('TimeService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ]
  }));

  it('should be created', () => {
    const service: MonthlyReportService = TestBed.get(MonthlyReportService);
    expect(service).toBeTruthy();
  });
});
