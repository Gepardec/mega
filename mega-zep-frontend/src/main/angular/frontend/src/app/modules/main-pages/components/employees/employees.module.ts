import {NgModule} from '@angular/core';
import {EmployeesListComponent} from "./components/employees-list/employees-list.component";
import {DisplayEmployeesComponent} from "./components/display-employees/display-employees.component";
import {EmployeesContainer} from "./employees.container";
import { EmployeesGridListComponent } from './components/employees-grid-list/employees-grid-list.component';
import { EmployeesTableListComponent } from './components/employees-table-list/employees-table-list.component';
import {CommonModule} from "@angular/common";
import {AngularMaterialModule} from "../../../../material-module";
import {UtilModule} from "../../../shared/util/util.module";
import {homeRouting} from "./employees.routing";

@NgModule({
  declarations: [DisplayEmployeesComponent, EmployeesListComponent, EmployeesContainer, EmployeesGridListComponent, EmployeesTableListComponent],
  imports: [
    CommonModule,
    AngularMaterialModule,
    UtilModule,
    homeRouting
  ],
  exports: [
    EmployeesContainer
  ]
})
export class EmployeesModule {
}
