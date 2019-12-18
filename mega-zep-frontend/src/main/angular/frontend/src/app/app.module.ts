import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthServiceConfig, GoogleLoginProvider, SocialLoginModule} from 'angularx-social-login';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AngularMaterialModule} from "./material-module";
import { environment } from '../environments/environment';
import {APP_BASE_HREF} from "@angular/common";
import {registerLocaleData} from "@angular/common";
import localeDeAt from "@angular/common/locales/de-AT";
import { MainPagesContainer } from './modules/main-pages/main-pages.container';
import {MainPagesModule} from "./modules/main-pages/main-pages.module";
import {SharedModule} from "./modules/shared/shared.module";
import {GlobalHttpInterceptorService} from "./modules/shared/interceptors/global-http-interceptor.service";
import {ErrorHandlerService} from "./modules/shared/services/error/error-handler.service";

registerLocaleData(localeDeAt, 'de-AT');

const config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider(environment.oauthClientId)
  },
]);

export function provideConfig() {
  return config;
}

@NgModule({
  declarations: [
    AppComponent,
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
    {provide: AuthServiceConfig, useFactory: provideConfig},
    {provide: ErrorHandler, useClass: ErrorHandlerService},
    {provide: HTTP_INTERCEPTORS, useClass: GlobalHttpInterceptorService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
