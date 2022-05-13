import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {GeneralInfoComponent} from './general-info.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MonthlyReport} from '../../models/MonthlyReport';
import {By} from '@angular/platform-browser';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {TranslateTestingModule} from 'ngx-translate-testing';
import {MatCardModule} from '@angular/material/card';
import {NgxSkeletonLoaderModule} from 'ngx-skeleton-loader';

describe('GeneralInfoComponent', () => {

  let component: GeneralInfoComponent;
  let fixture: ComponentFixture<GeneralInfoComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        GeneralInfoComponent
      ],
      imports: [
        TranslateTestingModule.withTranslations({de: require('src/assets/i18n/de.json')}),
        HttpClientTestingModule,
        MatCardModule,
        NgxSkeletonLoaderModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(GeneralInfoComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should display values from monthly report', () => {
    const monthlyReport = new MonthlyReport();

    monthlyReport.homeofficeDays = 10;
    monthlyReport.vacationDays = 3;
    monthlyReport.compensatoryDays = 2;
    monthlyReport.totalWorkingTime = '08:00';
    monthlyReport.billableTime = '04:00';

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
    monthlyReport.totalWorkingTime = '08:00';
    monthlyReport.billableTime = '04:00';

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

  it('#calculateBillingPercentage - should return 0', () => {
    fixture.detectChanges();

    const totalWorkingTime = '8000';
    const billableTime = '600';

    const billingPercentage = component.calculateBillingPercentage(totalWorkingTime, billableTime);

    expect(billingPercentage).toEqual(0);
  });

  function assertMonthlyReportRow(rowNumber: number, expectedHeader: string, expectedDays: string, expectedIdentifier: string) {
    const tr = fixture.debugElement.query(By.css(`tr:nth-of-type(${rowNumber})`));

    const header = tr.query(By.css('.headers')).nativeElement.innerHTML.trim();
    expect(header).toEqual(expectedHeader.trim());
    const days = tr.query(By.css('.infovalue')).nativeElement.innerHTML.trim();
    expect(days).toEqual(expectedDays.trim());
    const identifier = tr.query(By.css('.identifier')).nativeElement.innerHTML.trim();
    expect(identifier).toEqual(expectedIdentifier.trim());
  }
});
