import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainLayoutComponent} from "./shared/main-layout/main-layout/main-layout.component";
import {GoogleSigninComponent} from "./signin/google-signin/google-signin.component";
import {DashboardPagesModule} from "./main-pages/dashboard/home/home.module";
import {HomePagesModule} from "./main-pages/home/home.module";
import {GoogleUserAuthenticationGuard} from "./shared/guards/google-user-authentication.guard";


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
  { path: '', component: GoogleSigninComponent },

  { path: '#', component: GoogleSigninComponent },

  { path: 'login', component: GoogleSigninComponent},

  { path: '**', redirectTo: 'login' }

];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
