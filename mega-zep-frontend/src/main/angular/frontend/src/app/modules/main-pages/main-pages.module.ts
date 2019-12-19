import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {EmployeesModule} from "./components/employees/employees.module";
import {MonthlyReportModule} from "./components/monthly-report/monthly-report.module";
import {HeaderComponent} from "./components/header/header.component";
import {UserActionsComponent} from "./components/header/user-actions/user-actions.component";
import {AngularMaterialModule} from "../../material-module";
import {AppRoutingModule} from "../../app-routing.module";

@NgModule({
  declarations: [
    HeaderComponent,
    UserActionsComponent
  ],
  exports: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    EmployeesModule,
    MonthlyReportModule,
    AngularMaterialModule,
    AppRoutingModule
  ]
})
export class MainPagesModule { }
