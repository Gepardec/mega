import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {homeRouting} from "./home.routing";
import {DisplayMonthlyReportComponent} from "../display-monthly-report/display-monthly-report.component";
import {AngularMaterialModule} from "../../../material-module";
import {MonthlyReportService} from "../../../zep-services/monthly-report.service";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayMonthlyReportComponent
  ],
  imports: [
    CommonModule,
    homeRouting,
    AngularMaterialModule
  ],
  providers: [
    MonthlyReportService
  ]
})
export class MonthlyReportModule {
}
