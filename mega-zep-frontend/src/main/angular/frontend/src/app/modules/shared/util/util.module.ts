import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DatepickerComponent} from "./datepicker/datepicker.component";
import {AngularMaterialModule} from "../../../material-module";
import { CalenderComponent } from './calender/calender.component';



@NgModule({
  declarations: [
    DatepickerComponent,
    CalenderComponent
  ],
  exports: [
    DatepickerComponent,
    CalenderComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule
  ]
})
export class UtilModule { }
