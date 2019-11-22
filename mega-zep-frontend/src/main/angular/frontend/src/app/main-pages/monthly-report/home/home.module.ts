import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {homeRouting} from "./home.routing";
import {AppModule} from "../../../app.module";
import {TimeEntryService} from "../time-entry.service";
import {DisplayTimeEntryComponent} from "../display-time-entry/display-time-entry.component";
import {AngularMaterialModule} from "../../../material-module";


@NgModule({
  declarations: [
    HomeComponent,
    DisplayTimeEntryComponent
  ],
  imports: [
    CommonModule,
    homeRouting,
    AngularMaterialModule
  ],
  providers: [
    TimeEntryService
  ]
})
export class MonthlyReportModule {
}
