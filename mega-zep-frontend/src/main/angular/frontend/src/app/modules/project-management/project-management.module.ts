import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProjectManagementComponent} from './components/project-management.component';
import {AngularMaterialModule} from '../material/material-module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {SharedModule} from '../shared/shared.module';
import {TranslateModule} from '@ngx-translate/core';
import {FormsModule} from "@angular/forms";
import {BillableTimesComponent} from './components/billable-times/billable-times.component';

@NgModule({
  declarations: [ProjectManagementComponent, BillableTimesComponent],
  imports: [
    CommonModule,
    AngularMaterialModule,
    FlexLayoutModule,
    SharedModule,
    TranslateModule.forRoot(),
    FormsModule
  ]
})
export class ProjectManagementModule {
}
