import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EmployeesComponent } from './components/employees.component';
import { AngularMaterialModule } from '../material/material-module';
import { SharedModule } from '../shared/shared.module';
import { EmployeesModuleRouting } from './employees.routing.module';
import { EmployeesFilterComponent } from './components/employees-filter/employees-filter.component';
import { EmployeesTableComponent } from './components/employees-table/employees-table.component';
import { TranslateModule } from '@ngx-translate/core';
import { OfficeManagementModule } from '../office-management/office-management.module';

@NgModule({
  declarations: [
    EmployeesComponent,
    EmployeesFilterComponent,
    EmployeesTableComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    EmployeesModuleRouting,
    AngularMaterialModule,
    SharedModule,
    TranslateModule.forRoot(),
    OfficeManagementModule
  ],
  exports: [
    EmployeesComponent
  ]
})
export class EmployeesModule {
}
