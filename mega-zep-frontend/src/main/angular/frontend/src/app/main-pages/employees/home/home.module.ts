import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {DisplayEmployeeListComponent} from "../display-employee-list/display-employee-list.component";
import {DisplayEmployeeListService} from "../display-employee-list/display-employee-list.service";
import {homeRouting} from "./home.routing";
import {AngularMaterialModule} from "../../../material-module";
import {PaginationComponent} from "../pagination/pagination.component";
import {GridlistComponent} from "../views/gridlist/gridlist.component";
import {TablelistComponent} from "../views/tablelist/tablelist.component";
import {UtilModule} from "../../../shared/util/util.module";
import {DatePickerDialogComponent} from "../views/gridlist/date-picker-dialog/date-picker-dialog.component";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayEmployeeListComponent,
    PaginationComponent,
    GridlistComponent,
    TablelistComponent,
    DatePickerDialogComponent
  ],
  imports: [
    CommonModule,
    homeRouting,
    AngularMaterialModule,
    UtilModule
  ],
  entryComponents: [
    DatePickerDialogComponent
  ],
  providers: [
    DisplayEmployeeListService
  ]
})
export class EmployeesPagesModule { }
