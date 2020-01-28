import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { configuration } from './modules/shared/constants/configuration';
import { LoginComponent } from './modules/shared/components/login/login.component';
import { LoginGuard } from './modules/shared/guards/login.guard';
import { EmployeesComponent } from './modules/employees/employees.component';
import { MonthlyReportComponent } from './modules/monthly-report/monthly-report.component';
import { RolesGuard } from './modules/shared/guards/roles.guard';
import { Role } from './modules/shared/models/Role';


export const routes: Routes = [
  {
    path: configuration.PAGE_URLS.MONTHLY_REPORT,
    component: MonthlyReportComponent,
    loadChildren: './modules/monthly-report/monthly-report.module#MonthlyReportModule',
    canActivate: [LoginGuard]
  },
  {
    path: configuration.PAGE_URLS.EMPLOYEES,
    component: EmployeesComponent,
    loadChildren: './modules/employees/employees.module#EmployeesModule',
    data: {
      roles: [Role.ADMINISTRATOR, Role.CONTROLLER]
    },
    canActivate: [LoginGuard, RolesGuard]
  },
  {
    path: configuration.PAGE_URLS.LOGIN, component: LoginComponent
  },
  {path: '', redirectTo: configuration.PAGE_URLS.LOGIN, pathMatch: 'full'},
  {path: '#', redirectTo: configuration.PAGE_URLS.LOGIN},
  {path: '**', redirectTo: configuration.PAGE_URLS.LOGIN}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
