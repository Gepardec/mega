import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MonthlyReportContainer} from "./monthly-report.container";
import { DisplayMonthlyReportComponent } from './display-monthly-report/display-monthly-report.component';
import {AngularMaterialModule} from "../../../../material-module";
import {homeRouting} from "./monthly-report.routing";



@NgModule({
  declarations: [MonthlyReportContainer, DisplayMonthlyReportComponent],
  imports: [
    CommonModule,
    AngularMaterialModule,
    homeRouting
  ]
})
export class MonthlyReportModule { }
