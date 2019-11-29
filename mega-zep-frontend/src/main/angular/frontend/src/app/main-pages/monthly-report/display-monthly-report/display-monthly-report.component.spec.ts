import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayMonthlyReportComponent } from './display-monthly-report.component';

describe('DisplayTimeEntryComponent', () => {
  let component: DisplayMonthlyReportComponent;
  let fixture: ComponentFixture<DisplayMonthlyReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayMonthlyReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayMonthlyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
