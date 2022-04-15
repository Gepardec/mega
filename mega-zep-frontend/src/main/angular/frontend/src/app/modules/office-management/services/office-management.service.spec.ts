import {TestBed} from '@angular/core/testing';

import {OfficeManagementService} from './office-management.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ConfigService} from '../../shared/services/config/config.service';
import {ManagementEntry} from '../../shared/models/ManagementEntry';
import {State} from '../../shared/models/State';
import {Employee} from '../../shared/models/Employee';
import {PmProgress} from '../../monthly-report/models/PmProgress';
import {HttpStatusCode} from '@angular/common/http';

describe('OfficeManagementService', () => {

  let officeManagementService: OfficeManagementService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    officeManagementService = TestBed.inject(OfficeManagementService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(officeManagementService).toBeTruthy();
  });

  it('#getEntries - should return entries', (done) => {
    officeManagementService.getEntries(OfficeManagementMock.year, OfficeManagementMock.month)
      .subscribe(managementEntries => {
        expect(managementEntries).toEqual(OfficeManagementMock.managementEntries);
        done();
      });

    const testRequest = httpTestingController.expectOne(
      configService.getBackendUrlWithContext(`/management/officemanagemententries/${OfficeManagementMock.year}/${OfficeManagementMock.month}`));
    testRequest.flush(OfficeManagementMock.managementEntries);
  });

  it('#updateEmployees - should return response', (done) => {
    officeManagementService.updateEmployees([OfficeManagementMock.employee])
      .subscribe(response => {
        expect(response.status).toEqual(HttpStatusCode.Ok);
        done();
      });

    const testRequest = httpTestingController.expectOne(
      configService.getBackendUrlWithContext('/employees'));
    testRequest.flush({
      status: HttpStatusCode.Ok
    });
  });

  class OfficeManagementMock {

    static year: number = 2021;
    static month: number = 10;

    private static email: string = 'max.muster@gepardec.com';
    private static firstname: string = 'Max';
    private static lastname: string = 'Muster';

    static employee: Employee = {
      email: OfficeManagementMock.email,
      active: true,
      firstname: OfficeManagementMock.firstname,
      lastname: OfficeManagementMock.lastname,
      releaseDate: '2021-10-01',
      salutation: 'Herr',
      title: 'MSc',
      userId: '011-mmuster',
      workDescription: 'Software-Engineer'
    }

    static employeeProgresses: Array<PmProgress> = [
      {
        firstname: OfficeManagementMock.firstname,
        lastname: OfficeManagementMock.lastname,
        state: State.DONE,
        assigneeEmail: OfficeManagementMock.email,
        project: 'Liw-Microservices',
        stepId: 1
      }
    ]

    static managementEntries: Array<ManagementEntry> = [
      {
        employee: OfficeManagementMock.employee,
        internalCheckState: State.DONE,
        employeeCheckState: State.DONE,
        projectCheckState: State.DONE,
        customerCheckState: State.DONE,
        billableTime: '14',
        employeeProgresses: OfficeManagementMock.employeeProgresses,
        entryDate: '2021-10-01',
        finishedComments: 10,
        nonBillableTime: '14',
        totalComments: 20
      }
    ]
  }
});
