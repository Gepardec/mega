import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneralInfoComponent} from './general-info.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MonthlyReport} from '../../models/MonthlyReport';
import {By} from '@angular/platform-browser';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import { TranslateTestingModule } from 'ngx-translate-testing';


describe('GeneralInfoComponent', () => {
  let component: GeneralInfoComponent;
  let fixture: ComponentFixture<GeneralInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateTestingModule.withTranslations({ de: require('src/assets/i18n/de.json') }),
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display values from monthly report', () => {
    let monthlyReport = new MonthlyReport();
    monthlyReport.homeofficeDays = 10;
    monthlyReport.vacationDays = 3;
    monthlyReport.compensatoryDays = 2;

    component.monthlyReport = monthlyReport;

    fixture.detectChanges();

    assertMonthlyReportRow(5, 'Urlaub', '3', 'Tage');


    // tr:nth-of-type(5) -> Urlaub
    // tr:nth-of-type(6) -> ZAG
    // tr:nth-of-type(7) -> Homeoffice


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
