import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfficeManagementComponent } from './office-management.component';
import { AngularMaterialModule } from '../../material-module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [OfficeManagementComponent],
  exports: [
    OfficeManagementComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    TranslateModule.forRoot()
  ]
})
export class OfficeManagementModule {
}
