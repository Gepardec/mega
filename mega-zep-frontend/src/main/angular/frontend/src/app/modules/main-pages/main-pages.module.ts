import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {EmployeesModule} from "./components/employees/employees.module";
import {MonthlyReportModule} from "./components/monthly-report/monthly-report.module";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    EmployeesModule,
    MonthlyReportModule
  ]
})
export class MainPagesModule { }
