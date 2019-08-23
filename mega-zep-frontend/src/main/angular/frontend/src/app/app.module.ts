import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthServiceConfig, GoogleLoginProvider, LoginOpt, SocialLoginModule} from 'angularx-social-login';
import {GoogleSigninComponent} from './signin/google-signin/google-signin.component';
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLayoutModule} from "./shared/main-layout/main-layout/main-layout.module";
import {NavigationModule} from "./shared/navigation/navigation.module";
import {HomeComponent} from './main-pages/employees/home/home.component';
import { PaginationComponent } from './main-pages/employees/pagination/pagination.component';
import { GridlistComponent } from './main-pages/employees/views/gridlist/gridlist.component';
import { TablelistComponent } from './main-pages/employees/views/tablelist/tablelist.component';

let config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider('259022406161-oi9mt111p3j3ul93dikbf2etfjoo4vjm.apps.googleusercontent.com')
  },
]);

export function provideConfig() {
  return config;
}

const googleLoginOptions: LoginOpt = {
  scope: 'profile email'
};


@NgModule({
  declarations: [
    AppComponent,
    GoogleSigninComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule,
    BrowserAnimationsModule,
    NavigationModule,
    MainLayoutModule
  ],
  exports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule,
    BrowserAnimationsModule,
    NavigationModule,
    MainLayoutModule
  ],
  providers: [
    HttpClientModule,
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}