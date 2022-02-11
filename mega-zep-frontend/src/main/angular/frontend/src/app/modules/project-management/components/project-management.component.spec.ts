import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectManagementComponent} from './project-management.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../material/material-module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {expect} from "@angular/flex-layout/_private-utils/testing";
import * as _moment from "moment";
import {ConfigService} from "../../shared/services/config/config.service";
import {Config} from "../../shared/models/Config";
import {of} from "rxjs";
import {ProjectManagementService} from "../services/project-management.service";
import {Employee} from "../../shared/models/Employee";
import {PmProgress} from "../../monthly-report/models/PmProgress";
import {State} from "../../shared/models/State";
import {ManagementEntry} from "../../shared/models/ManagementEntry";
import {ProjectManagementEntry} from "../models/ProjectManagementEntry";
import {ProjectState} from "../../shared/models/ProjectState";
import {SelectionModel} from "@angular/cdk/collections";
import {CommentService} from "../../shared/services/comment/comment.service";
import {Comment} from "../../shared/models/Comment";
import {MatDialog} from "@angular/material/dialog";
import {RouterTestingModule} from "@angular/router/testing";
import {OAuthModule} from "angular-oauth2-oidc";

const moment = _moment;
const DATE_FORMAT: string = 'YYYY-MM-DD';

describe('ProjectManagementComponent', () => {

  let component: ProjectManagementComponent;
  let fixture: ComponentFixture<ProjectManagementComponent>;

  let dialog: MatDialog;
  let configService: ConfigService;
  let pmService: ProjectManagementService;
  let commentService: CommentService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot(),
        AngularMaterialModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ProjectManagementComponent);
      component = fixture.componentInstance;

      dialog = TestBed.inject(MatDialog);
      configService = TestBed.inject(ConfigService);
      pmService = TestBed.inject(ProjectManagementService);
      commentService = TestBed.inject(CommentService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#getDate - should return selected date with first day of month', () => {
    fixture.detectChanges();

    component.selectedYear = moment().year();
    component.selectedMonth = moment().month();

    expect(component.date).toEqual(moment().date(1).startOf('day'));
  });

  it('#afterInit - should call pmService.getEntries and set selectedYear and selectedMonth', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(pmService, 'getEntries').and.returnValue(of(ProjectManagementMock.projectManagementEntries));

    component.dateChanged(moment());
    flush();

    expect(pmService.getEntries).toHaveBeenCalled();
    expect(component.selectedYear).toBeTruthy();
    expect(component.selectedMonth).toBeTruthy();
  }));

  it('#areAllSelected - should return true', () => {
    fixture.detectChanges();

    component.pmEntries = ProjectManagementMock.projectManagementEntries;

    component.pmSelectionModels = new Map<string, SelectionModel<ManagementEntry>>();
    component.pmSelectionModels.set(ProjectManagementMock.project, new SelectionModel<ManagementEntry>());

    const selectionModel = new SelectionModel<ManagementEntry>();
    selectionModel.select(ProjectManagementMock.projectManagementEntries[0].entries[0]);

    spyOn(component.pmSelectionModels, 'get').and.returnValue(selectionModel);

    const areAllSelected = component.areAllSelected(ProjectManagementMock.project);

    expect(areAllSelected).toBeTrue();
  });

  it('#masterToggle - should clear selection model', () => {
    fixture.detectChanges();

    component.pmEntries = ProjectManagementMock.projectManagementEntries;

    component.pmSelectionModels = new Map<string, SelectionModel<ManagementEntry>>();
    component.pmSelectionModels.set(ProjectManagementMock.project, new SelectionModel<ManagementEntry>());

    const selectionModel = new SelectionModel<ManagementEntry>();
    selectionModel.select(ProjectManagementMock.projectManagementEntries[0].entries[0]);

    spyOn(component.pmSelectionModels, 'get').and.returnValue(selectionModel);

    component.masterToggle(ProjectManagementMock.project);

    expect(component.pmSelectionModels.get(ProjectManagementMock.project).selected.length).toBe(0);
  });

  it('#openDialog - should call commentService.getCommentsForEmployee and dialog.open', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(commentService, 'getCommentsForEmployee').and.returnValue(of(CommentsMock.get()));
    spyOn(dialog, 'open').and.callThrough();

    component.openDialog(EmployeeMock.employee, ProjectManagementMock.project);
    flush();

    expect(dialog.open).toHaveBeenCalled();
  }));

  class CommentsMock {

    static dateFormat: string = 'yyyy-MM-DD';

    static get(): Array<Comment> {
      return [
        {
          authorEmail: 'max@gepardec.com',
          authorName: 'max',
          id: 1,
          message: "text",
          isEditing: false,
          state: State.DONE,
          updateDate: moment.now().toString()
        },
        {
          authorEmail: 'susi@gepardec.com',
          authorName: 'susi',
          id: 2,
          message: "text",
          isEditing: false,
          state: State.DONE,
          updateDate: moment.now().toString()
        },
        {
          authorEmail: 'franz@gepardec.com',
          authorName: 'franz',
          id: 3,
          message: "text",
          isEditing: false,
          state: State.OPEN,
          updateDate: moment.now().toString()
        }
      ]
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

  class ProjectManagementMock {

    static email: string = 'max.muster@gepardec.com';
    static firstname: string = 'Max';
    static lastname: string = 'Muster';
    static project: string = 'LIW-Microservices';
    static year: number = 2021;
    static month: number = 10;

    static responseStatus: number = 200;

    static employee: Employee = {
      email: ProjectManagementMock.email,
      active: true,
      firstname: ProjectManagementMock.firstname,
      lastname: ProjectManagementMock.lastname,
      releaseDate: '2021-10-01',
      salutation: 'Herr',
      title: 'MSc',
      userId: '011-mmuster',
      workDescription: 'Software-Engineer'
    };

    static employeeProgresses: Array<PmProgress> = [
      {
        firstname: ProjectManagementMock.firstname,
        lastname: ProjectManagementMock.lastname,
        state: State.DONE,
        assigneeEmail: ProjectManagementMock.email,
        project: ProjectManagementMock.project,
        stepId: 1
      }
    ];

    static managementEntries: Array<ManagementEntry> = [
      {
        employee: ProjectManagementMock.employee,
        internalCheckState: State.DONE,
        employeeCheckState: State.DONE,
        projectCheckState: State.DONE,
        customerCheckState: State.DONE,
        billableTime: '14',
        employeeProgresses: ProjectManagementMock.employeeProgresses,
        entryDate: '2021-10-01',
        finishedComments: 10,
        nonBillableTime: '14',
        totalComments: 20
      }
    ];

    static projectManagementEntries: Array<ProjectManagementEntry> = [
      {
        entries: ProjectManagementMock.managementEntries,
        controlProjectState: ProjectState.DONE,
        controlBillingState: ProjectState.DONE,
        projectName: ProjectManagementMock.project,
        aggregatedBillableWorkTimeInSeconds: 10000,
        aggregatedNonBillableWorkTimeInSeconds: 3000,
        projectComment: null,
        presetControlProjectState: false,
        presetControlBillingState: true
      }
    ]
  }

  class EmployeeMock {
    static employee: Employee = {
      email: ProjectManagementMock.project,
      active: true,
      firstname: ProjectManagementMock.firstname,
      lastname: ProjectManagementMock.lastname,
      releaseDate: '2021-10-01',
      salutation: 'Herr',
      title: 'MSc',
      userId: '011-mmuster',
      workDescription: 'Software-Engineer'
    };
  }
});
