import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from '../../material-module';
import { LoginComponent } from './components/login/login.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { HeaderComponent } from './components/header/header.component';
import { UserActionsComponent } from './components/header/user-actions/user-actions.component';
import { RouterModule } from '@angular/router';
import { ErrorComponent } from './components/error/error.component';


@NgModule({
  declarations: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule
  ],
  exports: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent
  ]
})
export class SharedModule {
}
