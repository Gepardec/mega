import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from "./header/header.component";
import {UserActionsComponent} from "./header/user-actions/user-actions.component";
import {AngularMaterialModule} from "../../material-module";
import {AppRoutingModule} from "../../app-routing.module";


@NgModule({
  declarations: [
    HeaderComponent,
    UserActionsComponent
  ],
  exports: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    AppRoutingModule
  ]
})
export class NavigationModule {
}
