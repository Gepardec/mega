import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthServiceConfig, GoogleLoginProvider, LoginOpt, SocialLoginModule} from 'angularx-social-login';
import {GoogleSigninComponent} from './signin/google-signin/google-signin.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLayoutModule} from './shared/main-layout/main-layout/main-layout.module';
import {NavigationModule} from './shared/navigation/navigation.module';
import {ErrorHandleInterceptor} from './shared/interceptors/ErrorHandleInterceptor';
import {AngularMaterialModule} from "./material-module";
import { environment } from '../environments/environment';
import {APP_BASE_HREF} from "@angular/common";


const config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider(environment.oauthClientId)
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
    MainLayoutModule,
    AngularMaterialModule
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
    {provide: APP_BASE_HREF, useValue: '/'},
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    },
    {
      provide: HTTP_INTERCEPTORS, useClass: ErrorHandleInterceptor, multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
