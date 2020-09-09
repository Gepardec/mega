import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthlyReportComponent } from './monthly-report.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MonthlyReportService } from '../services/monthly-report.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { MonthlyReport } from '../models/MonthlyReport';

describe('MonthlyReportComponent', () => {
  let component: MonthlyReportComponent;
  let fixture: ComponentFixture<MonthlyReportComponent>;

  class MonthlyReportServiceMock {
    getAll(): Observable<MonthlyReport> {
      return new BehaviorSubject<MonthlyReport>(undefined);
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MonthlyReportComponent],
      providers: [
        {
          provide: MonthlyReportService, useClass: MonthlyReportServiceMock
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthlyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
