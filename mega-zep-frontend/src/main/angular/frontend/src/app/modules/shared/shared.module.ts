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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { InfoComponent } from './components/info/info.component';
import { HttpClientModule } from '@angular/common/http';
import { InfoDialogComponent } from './components/info-dialog/info-dialog.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { StateSelectComponent } from './components/state-select/state-select.component';
import { DoneCommentsIndicatorComponent } from './components/done-comments-indicator/done-comments-indicator.component';
import { StateIndicatorComponent } from './components/state-indicator/state-indicator.component';
import { DatepickerMonthYearComponent } from './components/datepicker-month-year/datepicker-month-year.component';
import {ConfirmDialogComponent} from "./components/confirm-dialog/confirm-dialog.component";
import { ProjectStateSelectComponent } from './components/project-state-select/project-state-select.component';

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
    CommentsForEmployeeComponent,
    StateSelectComponent,
    DoneCommentsIndicatorComponent,
    StateIndicatorComponent,
    DatepickerMonthYearComponent,
    ConfirmDialogComponent,
    ProjectStateSelectComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  exports: [
    DatepickerComponent,
    HeaderComponent,
    UserActionsComponent,
    LoginComponent,
    ErrorComponent,
    InfoComponent,
    InfoDialogComponent,
    StateSelectComponent,
    ProjectStateSelectComponent,
    DoneCommentsIndicatorComponent,
    StateIndicatorComponent,
    DatepickerMonthYearComponent,
  ],
  entryComponents: [
    CommentsForEmployeeComponent,
    InfoDialogComponent,
    ConfirmDialogComponent
  ]
})
export class SharedModule {
}
