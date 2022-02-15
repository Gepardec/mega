import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {MonthlyReportComponent} from './monthly-report.component';
import {MonthlyReportService} from '../services/monthly-report.service';
import {MonthlyReport} from "../models/MonthlyReport";
import {of} from "rxjs";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {Employee} from "../../shared/models/Employee";
import {TranslateModule} from "@ngx-translate/core";
import {AngularMaterialModule} from "../../material/material-module";
import {RouterTestingModule} from "@angular/router/testing";
import {OAuthModule} from "angular-oauth2-oidc";

describe('MonthlyReportComponent', () => {

  let component: MonthlyReportComponent;
  let fixture: ComponentFixture<MonthlyReportComponent>;

  let monthlyReportService: MonthlyReportService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        AngularMaterialModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(MonthlyReportComponent);
      component = fixture.componentInstance;

      monthlyReportService = TestBed.inject(MonthlyReportService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should call monthlyReportService.getAll', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(monthlyReportService, 'getAll').and.returnValue(of(MonthlyReportServiceMock.monthlyReport));

    component.ngOnInit();
    flush();

    expect(monthlyReportService.getAll).toHaveBeenCalled();
  }));

  it('#getAllTimeEntriesByDate - should call monthlyReportService.getAllByDate', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(monthlyReportService, 'getAllByDate').and.returnValue(of(MonthlyReportServiceMock.monthlyReport));

    component.getAllTimeEntriesByDate(TimeMock.year, TimeMock.month);
    flush();

    expect(monthlyReportService.getAllByDate).toHaveBeenCalled();
  }));

  it('#getAllTimeEntries - should call monthlyReportService.getAll', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(monthlyReportService, 'getAll').and.returnValue(of(MonthlyReportServiceMock.monthlyReport));

    component.getAllTimeEntries();
    flush();

    expect(monthlyReportService.getAll).toHaveBeenCalled();
  }));

  class EmployeeMock {
    static employee: Employee = {
      email: "LIW-Microservices",
      active: true,
      firstname: 'Max',
      lastname: 'Muster',
      releaseDate: '2021-10-01',
      salutation: 'Herr',
      title: 'MSc',
      userId: '011-mmuster',
      workDescription: 'Software-Engineer'
    };
  }

  class MonthlyReportServiceMock {

    static monthlyReport: MonthlyReport = {
      vacationDays: 1,
      totalWorkingTime: "08:00",
      timeWarnings: [],
      otherChecksDone: true,
      journeyWarnings: [],
      homeofficeDays: 2,
      employeeProgresses: [],
      employeeCheckState: "",
      employee: EmployeeMock.employee,
      billableTime: "",
      compensatoryDays: 5,
      comments: [],
      assigned: true
    }
  }

  class TimeMock {

    static year = 2020;
    static month = 2;
  }
});
