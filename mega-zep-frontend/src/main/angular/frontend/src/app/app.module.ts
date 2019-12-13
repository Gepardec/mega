import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthServiceConfig, GoogleLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {GoogleSigninComponent} from './signin/google-signin/google-signin.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ErrorHandleInterceptor} from './modules/shared/interceptors/ErrorHandleInterceptor';
import {AngularMaterialModule} from "./material-module";
import {APP_BASE_HREF} from "@angular/common";
import {registerLocaleData} from "@angular/common";
import localeDeAt from "@angular/common/locales/de-AT";
import { MainPagesContainer } from './modules/main-pages/main-pages.container';
import {MainPagesModule} from "./modules/main-pages/main-pages.module";
import {SharedModule} from "./modules/shared/shared.module";

registerLocaleData(localeDeAt, 'de-AT');

const config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider('259022406161-oi9mt111p3j3ul93dikbf2etfjoo4vjm.apps.googleusercontent.com')
  },
]);

export function provideConfig() {
  return config;
}

@NgModule({
  declarations: [
    AppComponent,
    GoogleSigninComponent,
    MainPagesContainer
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    MainPagesModule,
    SharedModule
  ],
  exports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule,
    BrowserAnimationsModule
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
