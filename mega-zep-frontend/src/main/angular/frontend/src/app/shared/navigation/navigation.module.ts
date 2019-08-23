import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from "./header/header.component";
import {UserActionsComponent} from "./header/user-actions/user-actions.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {AngularMaterialModule} from "../../material-module";
import {AppRoutingModule} from "../../app-routing.module";


@NgModule({
  declarations: [
    SidebarComponent,
    HeaderComponent,
    UserActionsComponent
  ],
  exports: [
    SidebarComponent
  ],
  imports: [
    CommonModule,
    AngularMaterialModule,
    AppRoutingModule
  ]
})
export class NavigationModule { }
