import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {DisplayMonthlyReportComponent} from './display-monthly-report.component';
import {State} from '../../../shared/models/State';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {SharedModule} from '../../../shared/shared.module';
import {expect} from '@angular/flex-layout/_private-utils/testing';

import * as _moment from 'moment';
import {MonthlyReportService} from '../../services/monthly-report.service';
import {MonthlyReportModule} from '../../monthly-report.module';
import {RouterTestingModule} from '@angular/router/testing';
import {OAuthModule} from 'angular-oauth2-oidc';

const moment = _moment;

describe('DisplayMonthlyReportComponent', () => {

  let component: DisplayMonthlyReportComponent;
  let fixture: ComponentFixture<DisplayMonthlyReportComponent>;

  let monthlyReportService: MonthlyReportService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [DisplayMonthlyReportComponent],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot(),
        OAuthModule.forRoot(),
        SharedModule,
        MonthlyReportModule,
        RouterTestingModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(DisplayMonthlyReportComponent);
      component = fixture.componentInstance;

      monthlyReportService = TestBed.inject(MonthlyReportService);
    });
  }));

  beforeEach(() => {
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
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#getDate - should selected date with day of month 1', () => {
    fixture.detectChanges();

    component.selectedMonth = DateMock.month;
    component.selectedYear = DateMock.year;

    expect(component.date).toEqual(moment().year(DateMock.year).month(DateMock.month).date(1).startOf('day'));
  });

  it('#isValidDate - should check if valid date', () => {
    fixture.detectChanges();

    const dateStr = `${DateMock.year}-0${DateMock.month}-01`;
    const isValid = component.isValidDate(dateStr);

    expect(isValid).toBeTrue();
  });

  it('#emitRefreshMonthlyReport - should call emit', () => {
    fixture.detectChanges();

    spyOn(component.refreshMonthlyReport, 'emit').and.stub();

    component.emitRefreshMonthlyReport();

    expect(component.refreshMonthlyReport.emit).toHaveBeenCalled();
  });

  it('#dateChanged - should call monthlyReportService.selectedYear.next and monthlyReportService.selectedMonth.next', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(monthlyReportService.selectedYear, 'next').and.stub();
    spyOn(monthlyReportService.selectedMonth, 'next').and.stub();

    component.dateChanged(moment());
    flush();

    expect(monthlyReportService.selectedYear.next).toHaveBeenCalled();
    expect(monthlyReportService.selectedMonth.next).toHaveBeenCalled();
  }));

  class DateMock {
    static month = 1;
    static year = 2020;
  }
});
