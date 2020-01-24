import {RouterModule, Routes} from '@angular/router';
import {MonthlyReportComponent} from './monthly-report.component';
import {ModuleWithProviders} from '@angular/core';

export const homeRoutes: Routes = [
  {
    path: '',
    component: MonthlyReportComponent,
    data: {
      pageTitle: 'Monthly Report'
    }
  },
];

export const MonthlyReportRoutingModule: ModuleWithProviders = RouterModule.forChild(homeRoutes);

