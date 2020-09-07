import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MonthlyReportComponent } from './monthly-report.component';
import { DisplayMonthlyReportComponent } from './display-monthly-report/display-monthly-report.component';
import { AngularMaterialModule } from '../../material-module';
import { MonthlyReportRoutingModule } from './monthly-report.routing';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    MonthlyReportComponent,
    DisplayMonthlyReportComponent
  ],
  imports: [
    CommonModule,
    MonthlyReportRoutingModule,
    AngularMaterialModule,
    TranslateModule.forChild()
  ]
})
export class MonthlyReportModule {
}
