import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeCardComponent } from './components/employee-card/employee-card.component';
import { AngularMaterialModule } from '../material/material-module';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from '../shared/shared.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import {FormsModule} from "@angular/forms";
import { OfficeManagementComponent } from './components/office-management/office-management.component';
import { ProjectOverviewCardComponent } from './components/project-overview-card/project-overview-card.component';
import { EnterpriseCardComponent } from './components/enterprise-card/enterprise-card.component';

@NgModule({
  declarations: [EmployeeCardComponent, OfficeManagementComponent, ProjectOverviewCardComponent, EnterpriseCardComponent],
  exports: [
    EmployeeCardComponent // TODO: check if export of the container (office-management) or only of the card component is necessary
  ],
    imports: [
        CommonModule,
        AngularMaterialModule,
        TranslateModule.forRoot(),
        SharedModule,
        FlexLayoutModule,
        FormsModule
    ]
})
export class OfficeManagementModule {
}
