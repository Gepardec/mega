import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MonthlyReportComponent } from './components/monthly-report.component';
import { DisplayMonthlyReportComponent } from './components/display-monthly-report/display-monthly-report.component';
import { AngularMaterialModule } from '../material/material-module';
import { TranslateModule } from '@ngx-translate/core';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [
    MonthlyReportComponent,
    DisplayMonthlyReportComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    FlexLayoutModule
  ]
})
export class MonthlyReportModule {
}
