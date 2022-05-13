import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {PmProgressComponent} from './pm-progress.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {TranslateModule} from '@ngx-translate/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SharedModule} from '../../shared.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MatDialogRef} from '@angular/material/dialog';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material/bottom-sheet';
import {PmProgress} from '../../../monthly-report/models/PmProgress';
import {State} from '../../models/State';
import {expect} from '@angular/flex-layout/_private-utils/testing';

describe('PmProgressComponent', () => {

  let component: PmProgressComponent;
  let fixture: ComponentFixture<PmProgressComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        PmProgressComponent
      ],
      imports: [
        BrowserAnimationsModule,
        SharedModule,
        AngularMaterialModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        {provide: MAT_BOTTOM_SHEET_DATA, useValue: {}},
        {provide: MatDialogRef, useValue: {}}
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(PmProgressComponent);
      component = fixture.componentInstance;
      component.pmProgresses = [];
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should set displayedEmployees with correct state', () => {
    component.pmProgresses = PmProgressMock.pmProgresses;

    component.ngOnInit();

    expect(component.displayedEmployees.every(employee =>
      employee.state === State.OPEN
    )).toBeTrue();
  });

  class PmProgressMock {

    static pmProgresses: Array<PmProgress> = [];

    constructor() {
      const pmProgress1 = new PmProgress();
      pmProgress1.firstname = 'Max';
      pmProgress1.lastname = 'Müller';
      pmProgress1.state = State.OPEN;
      pmProgress1.project = 'LIW-Allgemein';
      pmProgress1.stepId = 1;
      pmProgress1.assigneeEmail = 'max.mueller@gepardec.com';

      const pmProgress2 = new PmProgress();
      pmProgress2.firstname = 'Susi';
      pmProgress2.lastname = 'Maier';
      pmProgress2.state = State.OPEN;
      pmProgress2.project = 'LIW-Allgemein';
      pmProgress2.stepId = 1;
      pmProgress2.assigneeEmail = 'susi.maier@gepardec.com';

      PmProgressMock.pmProgresses.push(pmProgress1, pmProgress2);
    }
  }
});
