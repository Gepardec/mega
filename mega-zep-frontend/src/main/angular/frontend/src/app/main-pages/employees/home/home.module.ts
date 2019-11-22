import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {DisplayEmployeesComponent} from "../display-employees/display-employees.component";
import {DisplayEmployeesService} from "../display-employees/display-employees.service";
import {homeRouting} from "./home.routing";
import {AngularMaterialModule} from "../../../material-module";
import {EmployeeListComponent} from "../employee-list/employee-list.component";
import {GridlistComponent} from "../views/gridlist/gridlist.component";
import {TablelistComponent} from "../views/tablelist/tablelist.component";
import {UtilModule} from "../../../shared/util/util.module";
import {DatePickerDialogComponent} from "../views/gridlist/date-picker-dialog/date-picker-dialog.component";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayEmployeesComponent,
    EmployeeListComponent,
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
    DisplayEmployeesService
  ]
})
export class EmployeesPagesModule {
}
