import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {DisplayEmployeeListComponent} from "../display-employee-list/display-employee-list.component";
import {DisplayEmployeeListService} from "../display-employee-list/display-employee-list.service";
import {homeRouting} from "./home.routing";
import {AngularMaterialModule} from "../../../material-module";
import {TablelistComponent} from "../views/tablelist/tablelist.component";
import {UtilModule} from "../../../shared/util/util.module";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayEmployeeListComponent,
    TablelistComponent
  ],
  imports: [
    CommonModule,
    homeRouting,
    AngularMaterialModule,
    UtilModule
  ],
  entryComponents: [
  ],
  providers: [
    DisplayEmployeeListService
  ]
})
export class EmployeesPagesModule { }
