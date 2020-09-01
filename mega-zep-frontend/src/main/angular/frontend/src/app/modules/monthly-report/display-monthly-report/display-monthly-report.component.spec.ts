import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayMonthlyReportComponent } from './display-monthly-report.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatTableModule } from '@angular/material/table';

describe('DisplayMonthlyReportComponent', () => {
  let component: DisplayMonthlyReportComponent;
  let fixture: ComponentFixture<DisplayMonthlyReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DisplayMonthlyReportComponent],
      imports: [
        MatTableModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayMonthlyReportComponent);
    component = fixture.componentInstance;
    // component.monthlyReport = {
    //   timeWarnings: [],
    //   journeyWarnings: [],
    //   employee: {
    //     firstName: 'John',
    //     sureName: 'Doe',
    //     email: 'john.doe@gepardec.com',
    //     releaseDate: '2020-01-01',
    //     role: 0,
    //     salutation: undefined,
    //     title: undefined,
    //     userId: 'john.doe@gepardec.com',
    //     workDescription: undefined,
    //     active: undefined
    //   }
    // };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
