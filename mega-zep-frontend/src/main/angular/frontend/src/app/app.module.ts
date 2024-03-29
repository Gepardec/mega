import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {APP_BASE_HREF, registerLocaleData} from '@angular/common';
import localeDeAt from '@angular/common/locales/de-AT';
import {SharedModule} from './modules/shared/shared.module';
import {OAuthModule, OAuthModuleConfig} from 'angular-oauth2-oidc';
import {authConfigFactory} from './auth/auth.config.factory';
import {ConfigService} from './modules/shared/services/config/config.service';
import {ErrorHandlerService} from './modules/shared/services/error/error-handler.service';
import {MonthlyReportModule} from './modules/monthly-report/monthly-report.module';
import {AuthorizationHeaderInterceptor} from './modules/shared/interceptors/authorization-header.interceptor';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {OfficeManagementModule} from './modules/office-management/office-management.module';
import {ProjectManagementModule} from './modules/project-management/project-management.module';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {NgxSkeletonLoaderModule} from 'ngx-skeleton-loader';

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
    NgxSkeletonLoaderModule,
    SharedModule,
    OfficeManagementModule,
    MonthlyReportModule,
    ProjectManagementModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: httpTranslateLoader,
        deps: [HttpClient]
      }
    }),
    MatDialogModule,
    MatButtonModule
  ],
  providers: [
    {provide: APP_BASE_HREF, useValue: '/'},
    {provide: ErrorHandler, useClass: ErrorHandlerService},
    {provide: OAuthModuleConfig, useFactory: authConfigFactory, deps: [ConfigService]},
    {provide: HTTP_INTERCEPTORS, useClass: AuthorizationHeaderInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

// AOT compilation support
export function httpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
