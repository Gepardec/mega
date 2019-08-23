import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home.component";
import {homeRouting} from "./home.routing";


@NgModule({
  declarations: [
    HomeComponent,

  ],
  imports: [
    CommonModule,
    homeRouting
  ]
})
export class DashboardPagesModule {
}
