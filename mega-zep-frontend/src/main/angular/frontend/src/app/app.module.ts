import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthServiceConfig, GoogleLoginProvider, LoginOpt, SocialLoginModule} from 'angularx-social-login';
import {GoogleSigninComponent} from './signin/google-signin/google-signin.component';
import {DisplayMitarbeiterListeComponent} from './display-mitarbeiter-liste/display-mitarbeiter-liste.component';
import {DisplayMitarbeiterListeService} from "./display-mitarbeiter-liste/display-mitarbeiter-liste.service";
import {HttpClientModule} from "@angular/common/http";
import {SidebarComponent} from './shared/navigation/sidebar/sidebar.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLayoutComponent} from './shared/main-layout/main-layout/main-layout.component';
import {AngularMaterialModule} from "./material-module";
import {HeaderComponent} from './shared/navigation/header/header.component';

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
    DisplayMitarbeiterListeComponent,
    SidebarComponent,
    MainLayoutComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
  ],
  providers: [
    HttpClientModule,
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    },
    DisplayMitarbeiterListeService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
