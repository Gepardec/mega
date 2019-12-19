import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthlyReportContainer } from './monthly-report.container';

describe('MonthlyReportComponent', () => {
  let component: MonthlyReportContainer;
  let fixture: ComponentFixture<MonthlyReportContainer>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonthlyReportContainer ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthlyReportContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
