import {RouterModule, Routes} from '@angular/router';
import {MonthlyReportContainer} from "./monthly-report.container";
import {ModuleWithProviders} from '@angular/core';


export const homeRoutes: Routes = [
  {
    path: '',
    component: MonthlyReportContainer,
    data: {
      pageTitle: 'Monthly Report'
    }
  },
];

export const homeRouting: ModuleWithProviders = RouterModule.forChild(homeRoutes);

