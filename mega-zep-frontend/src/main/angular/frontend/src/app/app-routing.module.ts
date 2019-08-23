import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainLayoutComponent} from "./shared/main-layout/main-layout/main-layout.component";
import {GoogleSigninComponent} from "./signin/google-signin/google-signin.component";
import {DashboardPagesModule} from "./main-pages/dashboard/home/home.module";
import {HomePagesModule} from "./main-pages/home/home.module";
import {GoogleUserAuthenticationGuard} from "./shared/guards/google-user-authentication.guard";
import {EmployeesPagesModule} from "./main-pages/employees/home/home.module";


export const routes: Routes = [
  {
    path: 'home',
    component: MainLayoutComponent,
    loadChildren: () => HomePagesModule,
    data: { pageTitle: 'Home' },
    canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'dashboard',
    component: MainLayoutComponent,
    loadChildren: () => DashboardPagesModule,
    data: { pageTitle: 'Dashboard' },
    canActivate: [GoogleUserAuthenticationGuard]
  },
  {
    path: 'employees',
    component: MainLayoutComponent,
    loadChildren: () => EmployeesPagesModule,
    data: { pageTitle: 'Employees' },
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
