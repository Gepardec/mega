import {TestBed} from '@angular/core/testing';

import {EnterpriseEntriesService} from './enterprise-entries.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ConfigService} from '../../../shared/services/config/config.service';
import {EnterpriseEntry} from '../../models/EnterpriseEntry';
import {ProjectState} from '../../../shared/models/ProjectState';
import {HttpResponse} from '@angular/common/http';

describe('EnterpriseEntriesService', () => {

  let enterpriseEntriesService: EnterpriseEntriesService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    enterpriseEntriesService = TestBed.inject(EnterpriseEntriesService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  afterEach(() => {
    httpTestingController.verify();
  })

  it('#should be created', () => {
    expect(enterpriseEntriesService).toBeTruthy();
  });

  it('#getEnterpriseEntry - should return enterpriseEntry', (done) => {
    enterpriseEntriesService.getEnterpriseEntry(EnterpriseEntryMock.year, EnterpriseEntryMock.month)
      .subscribe(enterpriseEntry => {
        expect(enterpriseEntry).toEqual(EnterpriseEntryMock.enterpriseEntry);
        done();
      });

    const testRequest = httpTestingController.expectOne(
      configService.getBackendUrlWithContext(`/enterprise/entriesformonthyear/${EnterpriseEntryMock.year}/${EnterpriseEntryMock.month}`));
    testRequest.flush(EnterpriseEntryMock.enterpriseEntry);
  });

  it('#updateEnterpriseEntry - should return true', (done) => {
    enterpriseEntriesService.updateEnterpriseEntry(EnterpriseEntryMock.enterpriseEntry, EnterpriseEntryMock.year, EnterpriseEntryMock.month)
      .subscribe(success => {
        expect(success).toBe(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(
      configService.getBackendUrlWithContext(`/enterprise/entry/${EnterpriseEntryMock.year}/${EnterpriseEntryMock.month}`));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  class EnterpriseEntryMock {

    static year: number = 2021;
    static month: number = 12;

    static enterpriseEntry: EnterpriseEntry = {
      currentMonthYear: `${EnterpriseEntryMock.year}-${EnterpriseEntryMock.month}`,
      chargeabilityExternalEmployeesRecorded: ProjectState.DONE,
      payrollAccountingSent: ProjectState.DONE,
      zepMonthlyReportDone: ProjectState.DONE,
      zepTimesReleased: ProjectState.DONE
    }

  }
});
