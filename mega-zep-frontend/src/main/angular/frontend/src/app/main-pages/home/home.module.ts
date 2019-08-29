import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {homeRouting} from "./home.routing";
import {DisplayEmployeeListComponent} from "../employees/display-employee-list/display-employee-list.component";
import { DisplayEmployeeComponent } from './display-employee/display-employee.component';


@NgModule({
  declarations: [
    HomeComponent,
    DisplayEmployeeComponent
  ],
  imports: [
    CommonModule,
    homeRouting
  ]
})
export class HomePagesModule {
}
