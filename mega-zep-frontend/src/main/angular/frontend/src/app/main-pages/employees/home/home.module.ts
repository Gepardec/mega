import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {DisplayEmployeeListComponent} from "../display-mitarbeiter-liste/display-employee-list.component";
import {DisplayEmployeeListService} from "../display-mitarbeiter-liste/display-employee-list.service";
import {homeRouting} from "./home.routing";
import {AngularMaterialModule} from "../../../material-module";
import {PaginationComponent} from "../pagination/pagination.component";
import {GridlistComponent} from "../views/gridlist/gridlist.component";
import {TablelistComponent} from "../views/tablelist/tablelist.component";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayEmployeeListComponent,
    PaginationComponent,
    GridlistComponent,
    TablelistComponent
  ],
  imports: [
    CommonModule,
    homeRouting,
    AngularMaterialModule
  ],
  providers: [
    DisplayEmployeeListService
  ]
})
export class EmployeesPagesModule { }
