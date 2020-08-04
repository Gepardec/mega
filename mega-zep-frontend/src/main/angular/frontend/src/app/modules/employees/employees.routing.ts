import { RouterModule, Routes } from '@angular/router';
import { EmployeesComponent } from './employees.component';
import { ModuleWithProviders } from '@angular/core';


export const homeRoutes: Routes = [
  {
    path: '',
    component: EmployeesComponent,
    data: {
      pageTitle: 'Employees'
    }
  },
];

export const EmployeesModuleRouting: ModuleWithProviders = RouterModule.forChild(homeRoutes);
