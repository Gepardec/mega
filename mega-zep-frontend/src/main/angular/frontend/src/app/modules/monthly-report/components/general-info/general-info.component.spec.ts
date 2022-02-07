import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneralInfoComponent} from './general-info.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MonthlyReport} from '../../models/MonthlyReport';
import {By} from '@angular/platform-browser';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {TranslateTestingModule} from 'ngx-translate-testing';


describe('GeneralInfoComponent', () => {
  let component: GeneralInfoComponent;
  let fixture: ComponentFixture<GeneralInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateTestingModule.withTranslations({de: require('src/assets/i18n/de.json')}),
        HttpClientTestingModule
      ],
      declarations: [GeneralInfoComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GeneralInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should display values from monthly report', () => {
    const monthlyReport = new MonthlyReport();
    monthlyReport.homeofficeDays = 10;
    monthlyReport.vacationDays = 3;
    monthlyReport.compensatoryDays = 2;

    component.monthlyReport = monthlyReport;

    fixture.detectChanges();

    assertMonthlyReportRow(5, 'Urlaub', '3', 'Tage');
    assertMonthlyReportRow(6, 'Zeitausgleich', '2', 'Tage');
    assertMonthlyReportRow(7, 'Homeoffice', '10', 'Tage');

  });

  it('#afterInit - should display values from monthly report with one day', () => {
    const monthlyReport = new MonthlyReport();

    monthlyReport.homeofficeDays = 1;
    monthlyReport.vacationDays = 1;
    monthlyReport.compensatoryDays = 1;

    component.monthlyReport = monthlyReport;

    fixture.detectChanges();

    assertMonthlyReportRow(5, 'Urlaub', '1', 'Tag');
    assertMonthlyReportRow(6, 'Zeitausgleich', '1', 'Tag');
    assertMonthlyReportRow(7, 'Homeoffice', '1', 'Tag');
  });

  it('#afterInit - should display working times and chargeability ', () => {
    const monthlyReport = new MonthlyReport();

    monthlyReport.totalWorkingTime = '80:00';
    monthlyReport.billableTime = '60:00';

    component.monthlyReport = monthlyReport;
    component.ngOnInit();

    fixture.detectChanges();

    assertMonthlyReportRow(1, 'Gesamte Arbeitszeit', '80,00', 'Stunden');
    assertMonthlyReportRow(2, 'Fakturierbare Stunden', '60,00', 'Stunden');
    assertMonthlyReportRow(3, 'Chargeability', '75,00', '%');

  });

  function assertMonthlyReportRow(rowNumber: number, expectedHeader: string, expectedDays: string, expectedIdentifier: string) {
    const tr = fixture.debugElement.query(By.css(`tr:nth-of-type(${rowNumber})`));

    const header = tr.query(By.css('.headers')).nativeElement.innerHTML;
    expect(header).toEqual(expectedHeader);
    const days = tr.query(By.css('.infovalue')).nativeElement.innerHTML;
    expect(days).toEqual(expectedDays);
    const identifier = tr.query(By.css('.identifier')).nativeElement.innerHTML;
    expect(identifier).toEqual(expectedIdentifier);
  }
});
