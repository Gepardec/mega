import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {homeRouting} from "./home.routing";
import {DisplayEmployeeListComponent} from "../employees/display-mitarbeiter-liste/display-employee-list.component";


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    homeRouting
  ]
})
export class HomePagesModule {
}
