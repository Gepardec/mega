import {TestBed} from '@angular/core/testing';

import {MonthlyReportService} from './monthly-report.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {MonthlyReport} from "../models/MonthlyReport";
import {Employee} from "../../shared/models/Employee";
import {ConfigService} from "../../shared/services/config/config.service";

describe('MonthlyReportService', () => {

  let monthlyReportService: MonthlyReportService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    monthlyReportService = TestBed.inject(MonthlyReportService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(monthlyReportService).toBeTruthy();
  });

  it('#getAll - should return MonthlyReport', (done) => {
    monthlyReportService.getAll()
      .subscribe(monthlyReport => {
        expect(monthlyReport).toEqual(MonthlyReportMock.monthlyReport);
        done();
      });

    const testResult = httpTestingController.expectOne(configService.getBackendUrlWithContext('/worker/monthendreports'));
    testResult.flush(MonthlyReportMock.monthlyReport);
  });

  it('#getAll - should return MonthlyReport by date November for next month in current year', (done) => {
    monthlyReportService.getAllByDate(2021, 11)
      .subscribe(monthlyReport => {
        expect(monthlyReport).toEqual(MonthlyReportMock.monthlyReport);
        done();
      });

    const testResult = httpTestingController.expectOne(configService.getBackendUrlWithContext('/worker/monthendreports/2021/12'));
    testResult.flush(MonthlyReportMock.monthlyReport);
  });

  it('#getAll - should return MonthlyReport by date December for next month in next year', (done) => {
    monthlyReportService.getAllByDate(2021, 12)
      .subscribe(monthlyReport => {
        expect(monthlyReport).toEqual(MonthlyReportMock.monthlyReport);
        done();
      });

    const testResult = httpTestingController.expectOne(configService.getBackendUrlWithContext('/worker/monthendreports/2022/1'));
    testResult.flush(MonthlyReportMock.monthlyReport);
  });

  class MonthlyReportMock {

    static year: number = 2021;
    static month: number = 11;

    static employee: Employee = {
      email: 'max-muster@gepardec.com',
      active: true,
      firstname: 'Max',
      lastname: 'Muster',
      releaseDate: '2021-10-01',
      salutation: 'Herr',
      title: 'MSc',
      userId: '011-mmuster',
      workDescription: 'Software-Engineer'
    };

    static monthlyReport: MonthlyReport = {
      assigned: true,
      billableTime: '10:15',
      comments: null,
      compensatoryDays: 2,
      employee: MonthlyReportMock.employee,
      employeeCheckState: 'done',
      employeeProgresses: null,
      homeofficeDays: 3,
      journeyWarnings: null,
      otherChecksDone: true,
      timeWarnings: null,
      totalWorkingTime: '10:15',
      vacationDays: 3
    };
  }
});
