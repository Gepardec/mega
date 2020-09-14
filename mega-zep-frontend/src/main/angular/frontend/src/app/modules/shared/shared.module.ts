import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from '../material/material-module';
import { LoginComponent } from './components/login/login.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { HeaderComponent } from './components/header/header.component';
import { UserActionsComponent } from './components/user-actions/user-actions.component';
import { RouterModule } from '@angular/router';
import { ErrorComponent } from './components/error/error.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommentsForEmployeeComponent } from './components/comments-for-employee/comments-for-employee.component';
import { FormsModule } from '@angular/forms';
import { InfoComponent } from './components/info/info.component';
import { HttpClientModule } from '@angular/common/http';
import { InfoDialogComponent } from './components/info-dialog/info-dialog.component';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent,
    InfoComponent,
    InfoDialogComponent,
    ErrorComponent,
    CommentsForEmployeeComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    FlexLayoutModule,
    FormsModule,
    HttpClientModule
  ],
  exports: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent,
    InfoComponent,
    InfoDialogComponent,
  ],
  entryComponents: [
    CommentsForEmployeeComponent,
    InfoDialogComponent
  ]
})
export class SharedModule {
}
