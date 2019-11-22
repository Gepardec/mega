import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MainLayoutComponent} from "./shared/main-layout/main-layout.component";
import {GoogleSigninComponent} from "./signin/google-signin/google-signin.component";
import {GoogleUserAuthenticationGuard} from "./shared/guards/google-user-authentication.guard";
import {EmployeesPagesModule} from "./main-pages/employees/home/home.module";
import {configuration} from "../configuration/configuration";
import {MonthlyReportModule} from "./main-pages/monthly-report/home/home.module";

export const routes: Routes = [
  {
    path: 'employees',
    component: MainLayoutComponent,
    loadChildren: () => EmployeesPagesModule,
    data: {roles: [configuration.EMPLOYEE_ROLES.ADMINISTRATOR, configuration.EMPLOYEE_ROLES.CONTROLLER]},
    canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'monthlyReport',
    component: MainLayoutComponent,
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
