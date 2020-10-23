import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfficeManagementComponent } from './components/office-management.component';
import { AngularMaterialModule } from '../material/material-module';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from '../shared/shared.module';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
  declarations: [OfficeManagementComponent],
  exports: [
    OfficeManagementComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    TranslateModule.forRoot(),
    SharedModule,
    FlexLayoutModule
  ]
})
export class OfficeManagementModule {
}
