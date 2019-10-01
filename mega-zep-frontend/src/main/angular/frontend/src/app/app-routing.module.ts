import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainLayoutComponent} from "./shared/main-layout/main-layout/main-layout.component";
import {GoogleSigninComponent} from "./signin/google-signin/google-signin.component";
import {DashboardPagesModule} from "./main-pages/dashboard/home/home.module";
import {HomePagesModule} from "./main-pages/home/home.module";
import {GoogleUserAuthenticationGuard} from "./shared/guards/google-user-authentication.guard";
import {EmployeesPagesModule} from "./main-pages/employees/home/home.module";
import {configuration} from "../configuration/configuration";

// uncomment when home page or other pages needed
// for now route to employee site
export const routes: Routes = [
  {
    path: 'home',
    redirectTo: 'employees'
    // component: MainLayoutComponent,
    // loadChildren: () => HomePagesModule,
    // canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'dashboard',
    redirectTo: 'employees'
    // component: MainLayoutComponent,
    // loadChildren: () => DashboardPagesModule,
    // canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'employees',
    component: MainLayoutComponent,
    loadChildren: () => EmployeesPagesModule,
    data: { roles: [configuration.EMPLOYEE_ROLES.ADMINISTRATOR, configuration.EMPLOYEE_ROLES.CONTROLLER] },
    canActivate: [GoogleUserAuthenticationGuard]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full'},

  { path: '#', redirectTo: 'login' },

  { path: 'login', component: GoogleSigninComponent},

  { path: '**', redirectTo: 'login' }

];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
