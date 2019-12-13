import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePickerDialogComponent } from './components/date-picker-dialog/date-picker-dialog.component';
import {AngularMaterialModule} from "../../material-module";
import {UtilModule} from "../../shared/util/util.module";



@NgModule({
  declarations: [DatePickerDialogComponent],
  imports: [
    CommonModule,
    AngularMaterialModule,
    UtilModule
  ],
  exports: [
    DatePickerDialogComponent
  ],
  entryComponents: [
    DatePickerDialogComponent
  ]
})
export class SharedModule { }
