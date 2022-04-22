import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {EnterpriseCardComponent} from './enterprise-card.component';
import {OfficeManagementModule} from '../../office-management.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {expect} from '@angular/flex-layout/_private-utils/testing';

import * as _moment from 'moment';
import {of} from 'rxjs';
import {Config} from '../../../shared/models/Config';
import {OfficeManagementService} from '../../services/office-management.service';
import {EnterpriseEntriesService} from '../../services/enterprise-entries/enterprise-entries.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ConfigService} from '../../../shared/services/config/config.service';
import {TranslateService} from '@ngx-translate/core';
import {EnterpriseEntry} from '../../models/EnterpriseEntry';
import {ProjectState} from '../../../shared/models/ProjectState';
import {configuration} from '../../../shared/constants/configuration';
import {MatSelectChange} from '@angular/material/select';
import {EnterpriseStep} from '../../models/EnterpriseStep';
import {ProjectStateSelectComponent} from '../../../shared/components/project-state-select/project-state-select.component';
import {ChangeDetectorRef} from '@angular/core';

const moment = _moment;

describe('EnterpriseCardComponent', () => {

  let component: EnterpriseCardComponent;
  let fixture: ComponentFixture<EnterpriseCardComponent>;

  let configService: ConfigService;
  let officeManagementService: OfficeManagementService;
  let enterpriseEntryService: EnterpriseEntriesService;
  let snackBar: MatSnackBar;
  let translateService: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        EnterpriseCardComponent
      ],
      imports: [
        OfficeManagementModule,
        HttpClientTestingModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(EnterpriseCardComponent);
      component = fixture.componentInstance;

      configService = TestBed.inject(ConfigService);
      officeManagementService = TestBed.inject(OfficeManagementService);
      enterpriseEntryService = TestBed.inject(EnterpriseEntriesService);
      snackBar = TestBed.inject(MatSnackBar);
      translateService = TestBed.inject(TranslateService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#getDate - should selected date with day of month 1', () => {
    fixture.detectChanges();

    component.selectedMonth = DateMock.month;
    component.selectedYear = DateMock.year;

    expect(component.date).toEqual(moment().year(DateMock.year).month(DateMock.month).date(1).startOf('day'));
  });

  it('#afterInit - should call projectManagementService.getEntries and projectCommentService.get', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(configService, 'getConfig').and.returnValue(of(ConfigMock.config));
    spyOn(enterpriseEntryService, 'getEnterpriseEntry').and.returnValue(of(EnterpriseEntryMock.enterpriseEntry));

    component.ngOnInit();
    flush();

    expect(component.selectedYear).toEqual(moment().subtract(1, 'month').year());
    expect(component.selectedMonth).toEqual(moment().subtract(1, 'month').month() + 1);
    expect(configService.getConfig).toHaveBeenCalled();
    expect(enterpriseEntryService.getEnterpriseEntry).toHaveBeenCalled();
  }));

  it('#afterDestroy - should call dateSelectionSub.unsubscribe', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(component.dateSelectionSub, 'unsubscribe').and.callThrough();

    component.ngOnDestroy();
    flush();

    expect(component.dateSelectionSub.unsubscribe).toHaveBeenCalled();
  }));

  it('#dateChanged - should call omService.selectedYear.next and omService.selectedMonth.next', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(officeManagementService.selectedYear, 'next').and.stub();
    spyOn(officeManagementService.selectedMonth, 'next').and.stub();

    component.dateChanged(moment());
    flush();

    expect(officeManagementService.selectedYear.next).toHaveBeenCalled();
    expect(officeManagementService.selectedMonth.next).toHaveBeenCalled();
  }));

  it('#onChangeEnterpriseState - should call enterpriseEntryService.updateEnterpriseEntry', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(enterpriseEntryService, 'updateEnterpriseEntry').and.returnValue(of(false));

    component.enterpriseEntry = EnterpriseEntryMock.enterpriseEntry;

    const matSelectChange = new MatSelectChange(null, EnterpriseStep.ZEP_TIMES_RELEASED);
    component.onChangeEnterpriseState(matSelectChange, EnterpriseStep.ZEP_TIMES_RELEASED, new ProjectStateSelectComponent(fixture.debugElement.injector.get(ChangeDetectorRef)));
    flush();

    expect(enterpriseEntryService.updateEnterpriseEntry).toHaveBeenCalled();
  }));

  class EnterpriseEntryMock {

    static enterpriseEntry: EnterpriseEntry = {
      zepTimesReleased: ProjectState.DONE,
      zepMonthlyReportDone: ProjectState.DONE,
      payrollAccountingSent: ProjectState.DONE,
      chargeabilityExternalEmployeesRecorded: ProjectState.DONE,
      currentMonthYear: moment().format(configuration.dateFormat)
    }
  }

  class DateMock {
    static month = 1;
    static year = 2020;
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

  class ProjectStateSelectComponentMock {
    value: string;
  }
});
