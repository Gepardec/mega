import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePickerDialogComponent } from './components/date-picker-dialog/date-picker-dialog.component';
import {AngularMaterialModule} from "../../material-module";
import {UtilModule} from "./util/util.module";
import { GoogleSigninComponent } from './components/google-signin/google-signin.component';



@NgModule({
  declarations: [DatePickerDialogComponent, GoogleSigninComponent],
  imports: [
    CommonModule,
    AngularMaterialModule,
    UtilModule
  ],
  exports: [
    DatePickerDialogComponent,
    GoogleSigninComponent
  ],
  entryComponents: [
    DatePickerDialogComponent
  ]
})
export class SharedModule { }
