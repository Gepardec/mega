import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EmployeesListComponent } from './components/employees-list/employees-list.component';
import { DisplayEmployeesComponent } from './components/display-employees/display-employees.component';
import { EmployeesGridListComponent } from './components/employees-grid-list/employees-grid-list.component';
import { EmployeesTableListComponent } from './components/employees-table-list/employees-table-list.component';
import { EmployeesComponent } from './employees.component';
import { AngularMaterialModule } from '../../material-module';
import { SharedModule } from '../shared/shared.module';
import { EmployeesModuleRouting } from './employees.routing';

@NgModule({
  declarations: [
    DisplayEmployeesComponent,
    EmployeesListComponent,
    EmployeesComponent,
    EmployeesGridListComponent,
    EmployeesTableListComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    EmployeesModuleRouting,
    AngularMaterialModule,
    SharedModule
  ],
  exports: [
    EmployeesComponent
  ]
})
export class EmployeesModule {
}
