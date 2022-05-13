import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {TimeCheckComponent} from './time-check.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {TimeWarning} from '../../models/TimeWarning';
import {configuration} from '../../../shared/constants/configuration';

import * as _moment from 'moment';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {EmployeeCheckComponent} from '../employee-check/employee-check.component';
import {NgxSkeletonLoaderModule} from 'ngx-skeleton-loader';

const moment = _moment;

describe('TimeCheckComponent', () => {

  let component: TimeCheckComponent;
  let fixture: ComponentFixture<TimeCheckComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        TimeCheckComponent
      ],
      imports: [
        TranslateModule.forRoot(),
        AngularMaterialModule,
        NgxSkeletonLoaderModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(TimeCheckComponent);
      component = fixture.componentInstance;
    })
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#displayWarningsTooltip - should return null', () => {
    fixture.detectChanges();

    const warnings = component.displayWarningsTooltip(TimeWarningMock.timewarning);

    expect(warnings).toBeNull();
  });

  it('#displayWarningsTooltip - should return null', () => {
    fixture.detectChanges();

    const warnings = component.displayWarningsTooltip(TimeWarningMock.timewarningWithWarnings);

    TimeWarningMock.timewarningWithWarnings.warnings.forEach(warning => {
      expect(warnings).toContain(warning);
    });
  });

  class TimeWarningMock {
    static timewarning: TimeWarning = {
      date: moment().format(configuration.dateFormat),
      excessWorkTime: 12,
      missingBreakTime: 1,
      warnings: [],
      missingRestTime: 2
    }

    static timewarningWithWarnings: TimeWarning = {
      date: moment().format(configuration.dateFormat),
      excessWorkTime: 12,
      missingBreakTime: 1,
      warnings: ['hey', 'you', 'do', 'something'],
      missingRestTime: 2
    }
  }
});
