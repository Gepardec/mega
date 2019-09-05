import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DatepickerComponent} from "./datepicker/datepicker.component";
import {AngularMaterialModule} from "../../material-module";



@NgModule({
  declarations: [
    DatepickerComponent
  ],
  exports: [
    DatepickerComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule
  ]
})
export class UtilModule { }
