import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from '../../material-module';
import { LoginComponent } from './components/login/login.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { HeaderComponent } from './components/header/header.component';
import { UserActionsComponent } from './components/header/user-actions/user-actions.component';
import { RouterModule } from '@angular/router';
import { ErrorComponent } from './components/error/error.component';
import { TranslateModule } from '@ngx-translate/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CommentsForEmployeeComponent } from './components/comments-for-employee/comments-for-employee.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent,
    CommentsForEmployeeComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    FlexLayoutModule,
    FormsModule
  ],
  exports: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent
  ],
  entryComponents: [
    CommentsForEmployeeComponent
  ]
})
export class SharedModule {
}
