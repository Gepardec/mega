import {NgModule} from '@angular/core';
import {MainLayoutComponent} from "./main-layout.component";
import {NavigationModule} from "../navigation/navigation.module";
import {RouterModule} from "@angular/router";


@NgModule({
  declarations: [
    MainLayoutComponent
  ],
  imports: [
    NavigationModule,
    RouterModule
  ]
})
export class MainLayoutModule {
}
