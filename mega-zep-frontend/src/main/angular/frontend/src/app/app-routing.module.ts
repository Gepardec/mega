import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {GoogleSigninComponent} from "./modules/shared/components/google-signin/google-signin.component";
import {GoogleUserAuthenticationGuard} from "./modules/shared/guards/authentication/google-user-authentication.guard";
import {EmployeesModule} from "./modules/main-pages/components/employees/employees.module";
import {configuration} from "./modules/shared/constants/configuration";
import {MonthlyReportModule} from "./modules/main-pages/components/monthly-report/monthly-report.module";
import {MainPagesContainer} from "./modules/main-pages/main-pages.container";

export const routes: Routes = [
  {
    path: 'employees',
    component: MainPagesContainer,
    loadChildren: () => EmployeesModule,
    data: {roles: [configuration.EMPLOYEE_ROLES.ADMINISTRATOR, configuration.EMPLOYEE_ROLES.CONTROLLER]},
    canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'monthlyReport',
    component: MainPagesContainer,
    loadChildren: () => MonthlyReportModule,
    canActivate: [GoogleUserAuthenticationGuard]
  },

  {path: '', redirectTo: 'login', pathMatch: 'full'},

  {path: '#', redirectTo: 'login'},

  {path: 'login', component: GoogleSigninComponent},

  {path: '**', redirectTo: 'login'}

];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
