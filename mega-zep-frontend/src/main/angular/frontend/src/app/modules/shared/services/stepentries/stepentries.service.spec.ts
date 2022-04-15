import {TestBed} from '@angular/core/testing';

import {StepentriesService} from './stepentries.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ConfigService} from '../config/config.service';
import {Employee} from '../../models/Employee';
import {Step} from '../../models/Step';
import {HttpResponse} from '@angular/common/http';

describe('StepentriesService', () => {

  let stepentriesService: StepentriesService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    stepentriesService = TestBed.inject(StepentriesService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(stepentriesService).toBeTruthy();
  });

  it('#close - should return true', (done) => {
    stepentriesService.close(StepentriesMock.employee, Step.ACCEPT_TIMES, StepentriesMock.monthYear)
      .subscribe(success => {
        expect(success).toEqual(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/stepentry/close'));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  it('#closeOfficeCheck - should return true', (done) => {
    stepentriesService.closeOfficeCheck(StepentriesMock.employee, Step.ACCEPT_TIMES, StepentriesMock.monthYear)
      .subscribe(success => {
        expect(success).toEqual(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/stepentry/closeforoffice'));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  it('#closeProjectCheck - should return true', (done) => {
    stepentriesService.closeProjectCheck(StepentriesMock.employee, StepentriesMock.projectName, StepentriesMock.monthYear)
      .subscribe(success => {
        expect(success).toEqual(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/stepentry/closeforproject'));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  class StepentriesMock {

    static projectName: string = 'LIW-Microservices';
    static monthYear: string = '2021-11';

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
  }
});
