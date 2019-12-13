import { TestBed } from '@angular/core/testing';

import { MonthlyReportService } from './monthly-report.service';

describe('TimeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MonthlyReportService = TestBed.get(MonthlyReportService);
    expect(service).toBeTruthy();
  });
});
