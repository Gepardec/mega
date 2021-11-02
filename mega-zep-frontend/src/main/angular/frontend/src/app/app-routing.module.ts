import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { configuration } from './modules/shared/constants/configuration';
import { LoginComponent } from './modules/shared/components/login/login.component';
import { LoginGuard } from './modules/shared/guards/login.guard';
import { RolesGuard } from './modules/shared/guards/roles.guard';
import { ErrorComponent } from './modules/shared/components/error/error.component';
import { MonthlyReportComponent } from './modules/monthly-report/components/monthly-report.component';
import { OfficeManagementComponent } from './modules/office-management/components/office-management/office-management.component';
import { ProjectManagementComponent } from './modules/project-management/components/project-management.component';
import { Role } from './modules/shared/models/Role';

export const routes: Routes = [
  {
    path: configuration.PAGE_URLS.MONTHLY_REPORT,
    component: MonthlyReportComponent,
    data: {
      role: Role.EMPLOYEE
    },
    canActivate: [LoginGuard, RolesGuard]
  },
  {
    path: configuration.PAGE_URLS.OFFICE_MANAGEMENT,
    component: OfficeManagementComponent,
    data: {
      role: Role.OFFICE_MANAGEMENT
    },
    canActivate: [LoginGuard, RolesGuard]
  },
  {
    path: configuration.PAGE_URLS.PROJECT_MANAGEMENT,
    component: ProjectManagementComponent,
    data: {
      role: Role.PROJECT_LEAD
    },
    canActivate: [LoginGuard, RolesGuard]
  },
  {
    path: configuration.PAGE_URLS.LOGIN,
    component: LoginComponent
  },
  {
    path: configuration.PAGE_URLS.ERROR,
    component: ErrorComponent
  },
  {
    path: '',
    redirectTo: configuration.PAGE_URLS.LOGIN,
    pathMatch: 'full'
  },
  {
    path: '#',
    redirectTo: configuration.PAGE_URLS.LOGIN
  },
  {
    path: '**',
    redirectTo: configuration.PAGE_URLS.LOGIN
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
