import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { APP_BASE_HREF, registerLocaleData } from '@angular/common';
import localeDeAt from '@angular/common/locales/de-AT';
import { SharedModule } from './modules/shared/shared.module';
import { OAuthModule, OAuthModuleConfig } from 'angular-oauth2-oidc';
import { EmployeesModule } from './modules/employees/employees.module';
import { GlobalHttpInterceptorService } from './modules/shared/interceptors/global-http-interceptor.service';
import { authConfigFactory } from './auth/auth.config.factory';
import { ConfigService } from './modules/shared/services/config/config.service';
import { ErrorHandlerService } from './modules/shared/services/error/error-handler.service';
import { MonthlyReportModule } from './modules/monthly-report/monthly-report.module';
import { AuthorizationHeaderInterceptor } from './modules/shared/interceptors/authorization-header.interceptor';

registerLocaleData(localeDeAt, 'de-AT');

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    OAuthModule.forRoot(),
    BrowserAnimationsModule,
    SharedModule,
    EmployeesModule,
    MonthlyReportModule
  ],
  providers: [
    {provide: APP_BASE_HREF, useValue: '/'},
    {provide: ErrorHandler, useClass: ErrorHandlerService},
    {provide: OAuthModuleConfig, useFactory: authConfigFactory, deps: [ConfigService]},
    {provide: HTTP_INTERCEPTORS, useClass: AuthorizationHeaderInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: GlobalHttpInterceptorService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
