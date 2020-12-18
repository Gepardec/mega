import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MonthlyReportComponent } from './components/monthly-report.component';
import { DisplayMonthlyReportComponent } from './components/display-monthly-report/display-monthly-report.component';
import { AngularMaterialModule } from '../material/material-module';
import { TranslateModule } from '@ngx-translate/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedModule } from '../shared/shared.module';
import { TimeCheckComponent } from './components/time-check/time-check.component';
import { JourneyCheckComponent } from './components/journey-check/journey-check.component';
import { EmployeeCheckComponent } from './components/employee-check/employee-check.component';
import { PmProgressComponent } from '../shared/components/pm-progress/pm-progress.component';

@NgModule({
  declarations: [
    MonthlyReportComponent,
    DisplayMonthlyReportComponent,
    TimeCheckComponent,
    JourneyCheckComponent,
    EmployeeCheckComponent,
    PmProgressComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    FlexLayoutModule,
    SharedModule
  ],
  entryComponents: [
    PmProgressComponent
  ]
})
export class MonthlyReportModule {
}
