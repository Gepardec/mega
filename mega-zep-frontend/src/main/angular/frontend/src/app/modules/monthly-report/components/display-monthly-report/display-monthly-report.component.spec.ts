import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DisplayMonthlyReportComponent} from './display-monthly-report.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {State} from '../../../shared/models/State';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {SharedModule} from "../../../shared/shared.module";

describe('DisplayMonthlyReportComponent', () => {
  let component: DisplayMonthlyReportComponent;
  let fixture: ComponentFixture<DisplayMonthlyReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DisplayMonthlyReportComponent],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot(),
        SharedModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayMonthlyReportComponent);
    component = fixture.componentInstance;
    component.monthlyReport = {
      employeeCheckState: State.OPEN,
      otherChecksDone: true,
      comments: [],
      timeWarnings: [
        {date: new Date().toString(), excessWorkTime: 12, missingBreakTime: 12, missingRestTime: 14, warnings: []},
      ],
      journeyWarnings: [
        {date: new Date().toString(), warnings: ['Bitte', 'hoer', 'auf']}
      ],
      employee: {
        userId: '000-mustermann',
        firstname: 'Max',
        lastname: 'Mustermann',
        salutation: null,
        releaseDate: '2020-07-01',
        workDescription: '05',
        email: 'mario.aslan@gepardec.com',
        title: null,
        active: true
      },
      assigned: false,
      employeeProgresses: null,
      billableTime: '10:00',
      compensatoryDays: 5,
      homeofficeDays: 3,
      vacationDays: 2,
      totalWorkingTime: '20:00'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
