import {RouterModule, Routes} from '@angular/router';
import {EmployeesContainer} from "./employees.container";
import {ModuleWithProviders} from '@angular/core';


export const homeRoutes: Routes = [
  {
    path: '',
    component: EmployeesContainer,
    data: {
      pageTitle: 'Employees'
    }
  },
];

export const homeRouting: ModuleWithProviders = RouterModule.forChild(homeRoutes);
