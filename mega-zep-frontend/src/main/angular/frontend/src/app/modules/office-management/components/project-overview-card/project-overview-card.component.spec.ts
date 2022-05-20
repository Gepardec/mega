import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectOverviewCardComponent} from './project-overview-card.component';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {OfficeManagementModule} from '../../office-management.module';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ProjectManagementService} from '../../../project-management/services/project-management.service';
import {ConfigService} from '../../../shared/services/config/config.service';
import {ProjectCommentService} from '../../../shared/services/project-comment/project-comment.service';
import {Config} from '../../../shared/models/Config';
import {of} from 'rxjs';
import {ManagementEntry} from '../../../shared/models/ManagementEntry';
import {State} from '../../../shared/models/State';
import {Employee} from '../../../shared/models/Employee';
import {ProjectManagementEntry} from '../../../project-management/models/ProjectManagementEntry';
import {ProjectState} from '../../../shared/models/ProjectState';
import {ProjectComment} from '../../../shared/models/ProjectComment';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {SnackbarService} from '../../../shared/services/snackbar/snackbar.service';
import {NgxSkeletonLoaderModule} from 'ngx-skeleton-loader';

describe('ProjectOverviewCardComponent', () => {

  let component: ProjectOverviewCardComponent;
  let fixture: ComponentFixture<ProjectOverviewCardComponent>;

  let configService: ConfigService;
  let projectManagementService: ProjectManagementService;
  let projectCommentService: ProjectCommentService;
  let translateService: TranslateService;
  let snackbarService: SnackbarService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProjectOverviewCardComponent
      ],
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        OfficeManagementModule,
        RouterTestingModule,
        NgxSkeletonLoaderModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ProjectOverviewCardComponent);
      component = fixture.componentInstance;

      configService = TestBed.inject(ConfigService);
      projectManagementService = TestBed.inject(ProjectManagementService);
      projectCommentService = TestBed.inject(ProjectCommentService);
      translateService = TestBed.inject(TranslateService);
      snackbarService = TestBed.inject(SnackbarService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should call projectManagementService.getEntries and projectCommentService.get', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(configService, 'getConfig').and.returnValue(of(ConfigMock.config));
    spyOn(projectManagementService, 'getEntries').and.returnValue(of(ProjectManagementEntryMock.projectManagementEntries));
    spyOn(projectCommentService, 'get').and.returnValue(of(ProjectCommentMock.projectComment));

    component.ngOnInit();
    flush();

    expect(configService.getConfig).toHaveBeenCalled();
    expect(projectManagementService.getEntries).toHaveBeenCalled();
    expect(projectCommentService.get).toHaveBeenCalled();
  }));

  it('#afterDestroy - should call dateSelectionSub.unsubscribe', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(component.dateSelectionSub, 'unsubscribe').and.callThrough();

    component.ngOnDestroy();
    flush();

    expect(component.dateSelectionSub.unsubscribe).toHaveBeenCalled();
  }));

  it('#isAtLeastOneEmployeeCheckDone - should check if at least one employee is checked', () => {
    fixture.detectChanges();

    const checkDone: ProjectState = component.isAtLeastOneEmployeeCheckDone(ProjectManagementEntryMock.projectManagementEntries[0]);

    expect(checkDone).toEqual(ProjectState.DONE);
  });

  it('#onStartEditing - should set forProjectName and showCommentEditor', () => {
    fixture.detectChanges();

    expect(component.forProjectName).not.toEqual(ProjectCommentMock.projectName);
    expect(component.showCommentEditor).toBeFalse();

    component.onStartEditing(ProjectCommentMock.projectName);

    expect(component.forProjectName).toEqual(ProjectCommentMock.projectName);
    expect(component.showCommentEditor).toBeTrue();
  });

  it('#onCommentChange - should call projectCommentService.update and call snackbarService.showSnackbarWithMessage', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(snackbarService, 'showSnackbarWithMessage').and.stub();

    const projectManagementEntry: ProjectManagementEntry = ProjectManagementEntryMock.projectManagementEntries[0];
    projectManagementEntry.projectComment = ProjectCommentMock.projectComment;

    component.onCommentChange(projectManagementEntry, ProjectCommentMock.projectComment2.comment);
    flush();

    expect(snackbarService.showSnackbarWithMessage).toHaveBeenCalled();
  }));

  it('#onCommentChange - should set right clicked to true and call snackbarService.showSnackbarWithMessage', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(snackbarService, 'showSnackbarWithMessage').and.stub();

    const pmEntry = ProjectManagementEntryMock.projectManagementEntries[0];
    pmEntry.projectComment = ProjectCommentMock.projectComment;

    component.onCommentChange(pmEntry, ProjectCommentMock.comment);

    expect(snackbarService.showSnackbarWithMessage).toHaveBeenCalled();
  }));

  it('#getTooltipText - should getTooltipText', () => {
    fixture.detectChanges();

    const toolTipText = component.getTooltipText(ProjectState.DONE);

    expect(toolTipText).toEqual(translateService.instant('STATE.' + ProjectState.DONE));
  });

  class ProjectCommentMock {

    static comment: string = 'this is a comment';
    static anotherComment: string = 'another comment';
    static monthYear: string = '2021-11';
    static projectName: string = 'LIW-Microservices'

    static projectComment: ProjectComment = {
      comment: ProjectCommentMock.comment,
      projectName: ProjectCommentMock.projectName,
      id: 1,
      date: '2021-11-23'
    }

    static projectComment2: ProjectComment = {
      comment: ProjectCommentMock.anotherComment,
      projectName: ProjectCommentMock.projectName,
      id: 1,
      date: '2021-11-23'
    }
  }

  class ConfigMock {

    static sessionStorageKey: string = 'MEGA_CONFIG';
    static frontendOriginSegment: number = 9876;
    static context: string = '/context/employee'

    static config: Config = {
      budgetCalculationExcelUrl: 'budgetCalculationExcelUrl',
      clientId: 'clientId',
      excelUrl: 'excelUrl',
      issuer: 'issuer',
      scope: 'scope',
      version: 'version',
      zepOrigin: 'zepOrigin'
    }
  }

  class EmployeeMock {
    static employee: Employee = {
      email: 'LIW-Microservices',
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

  class ManagementEntryMock {
    static managementEntries: Array<ManagementEntry> = [
      {
        employee: EmployeeMock.employee,
        internalCheckState: State.DONE,
        employeeCheckState: State.DONE,
        projectCheckState: State.DONE,
        customerCheckState: State.DONE,
        billableTime: '15',
        employeeProgresses: null,
        entryDate: '2021-10-2',
        finishedComments: 11,
        nonBillableTime: '15',
        totalComments: 22
      },
      {
        employee: EmployeeMock.employee,
        internalCheckState: State.DONE,
        employeeCheckState: State.DONE,
        projectCheckState: State.DONE,
        customerCheckState: State.DONE,
        billableTime: '15',
        employeeProgresses: null,
        entryDate: '2021-10-2',
        finishedComments: 11,
        nonBillableTime: '15',
        totalComments: 22
      },
      {
        employee: EmployeeMock.employee,
        internalCheckState: State.DONE,
        employeeCheckState: State.DONE,
        projectCheckState: State.DONE,
        customerCheckState: State.DONE,
        billableTime: '15',
        employeeProgresses: null,
        entryDate: '2021-10-2',
        finishedComments: 11,
        nonBillableTime: '15',
        totalComments: 22
      },
    ];
  }

  class ProjectManagementEntryMock {
    static projectManagementEntries: Array<ProjectManagementEntry> = [
      {
        entries: ManagementEntryMock.managementEntries,
        controlProjectState: ProjectState.DONE,
        controlBillingState: ProjectState.DONE,
        projectName: 'LIW-Allgemein',
        aggregatedBillableWorkTimeInSeconds: 10000,
        aggregatedNonBillableWorkTimeInSeconds: 3000,
        projectComment: null,
        presetControlProjectState: false,
        presetControlBillingState: true
      }
    ]
  }
});
