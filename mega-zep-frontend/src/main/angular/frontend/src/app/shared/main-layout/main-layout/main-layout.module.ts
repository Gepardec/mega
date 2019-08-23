import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MainLayoutComponent} from "./main-layout.component";
import {AngularMaterialModule} from "../../../material-module";
import {NavigationModule} from "../../navigation/navigation.module";



@NgModule({
  declarations: [
    MainLayoutComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    NavigationModule
  ]
})
export class MainLayoutModule { }
