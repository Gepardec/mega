import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {EmployeeCheckComponent} from './employee-check.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {OAuthModule} from "angular-oauth2-oidc";
import {Comment} from "../../../shared/models/Comment";
import {State} from "../../../shared/models/State";
import * as _moment from "moment";
import {configuration} from "../../../shared/constants/configuration";
import {expect} from "@angular/flex-layout/_private-utils/testing";
import {CommentService} from "../../../shared/services/comment/comment.service";
import {of} from "rxjs";
import {MonthlyReport} from "../../models/MonthlyReport";
import {StepentriesService} from "../../../shared/services/stepentries/stepentries.service";
import {Employee} from "../../../shared/models/Employee";
import {MatBottomSheet, MatBottomSheetRef} from "@angular/material/bottom-sheet";

const moment = _moment;
const DATE_FORMAT: string = configuration.dateFormat;

describe('EmployeeCheckComponent', () => {

  let component: EmployeeCheckComponent;
  let fixture: ComponentFixture<EmployeeCheckComponent>;

  let commentService: CommentService;
  let stepentriesService: StepentriesService;
  let bottomSheet: MatBottomSheet;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        EmployeeCheckComponent
      ],
      imports: [
        TranslateModule.forChild(),
        AngularMaterialModule,
        HttpClientTestingModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(EmployeeCheckComponent);
      component = fixture.componentInstance;

      commentService = TestBed.inject(CommentService);
      stepentriesService = TestBed.inject(StepentriesService);
      bottomSheet = TestBed.inject(MatBottomSheet);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#selectionChange - should set all done and call refreshMonthlyReport.emit', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(component.refreshMonthlyReport, 'emit').and.stub();
    spyOn(commentService, 'setStatusDone').and.returnValue(of(null));

    component.monthlyReport = new MonthlyReport();
    component.monthlyReport.comments = CommentsMock.setupComments();

    const change = {
      options: [
        {
          value: CommentsMock.setupComments()[0]
        }
      ]
    }

    component.selectionChange(change as any);
    flush();

    expect(component.refreshMonthlyReport.emit).toHaveBeenCalled();
  }));

  it('#setOpenAndUnassignedStepEntriesDone - should call refreshMonthlyReport.emit', fakeAsync(() => {
    fixture.detectChanges();

    component.monthlyReport = new MonthlyReport();
    component.monthlyReport.employee = EmployeeMock.employee;

    spyOn(component.refreshMonthlyReport, 'emit').and.stub();
    spyOn(stepentriesService, 'close').and.returnValue(of(null));

    component.setOpenAndUnassignedStepEntriesDone();
    flush();

    expect(component.refreshMonthlyReport.emit).toHaveBeenCalled();
  }));

  it('#openEmployeeProgress - should call bottomSheet.open', fakeAsync(() => {
    fixture.detectChanges();

    component.monthlyReport = new MonthlyReport();

    spyOn(bottomSheet, 'open').and.stub();

    component.openEmployeeProgress();

    expect(bottomSheet.open).toHaveBeenCalled();
  }));

  it('#parseBody - should replace link and add href', () => {
    fixture.detectChanges();

    const replacedString = component.parseBody('https://gepardec.com/info');

    expect(replacedString).toContain('href');
  });

  class CommentsMock {

    static setupComments(): Array<Comment> {
      return [
        {
          id: 1,
          message: "Hello",
          state: State.DONE,
          authorEmail: "max.mustermann@gepardec.com",
          updateDate: moment().format(DATE_FORMAT),
          isEditing: true,
          authorName: "Max Mustermann"
        },
        {
          id: 1,
          message: "World",
          state: State.DONE,
          authorEmail: "max.mustermann@gepardec.com",
          updateDate: moment().format(DATE_FORMAT),
          isEditing: true,
          authorName: "Max Mustermann"
        }
      ]
    }
  }

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
});
