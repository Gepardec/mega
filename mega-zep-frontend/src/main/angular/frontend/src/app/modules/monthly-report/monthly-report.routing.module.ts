import { RouterModule, Routes } from '@angular/router';
import { MonthlyReportComponent } from './components/monthly-report.component';
import { ModuleWithProviders } from '@angular/core';

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

