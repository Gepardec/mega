import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EmployeesComponent } from './employees.component';
import { AngularMaterialModule } from '../../material-module';
import { SharedModule } from '../shared/shared.module';
import { EmployeesModuleRouting } from './employees.routing';
import { EmployeesFilterComponent } from './components/employees-filter/employees-filter.component';
import { EmployeesTableComponent } from './components/employees-filter/employees-table/employees-table.component';
import { TranslateModule } from '@ngx-translate/core';

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
    TranslateModule.forChild()
  ],
  exports: [
    EmployeesComponent
  ]
})
export class EmployeesModule {
}
