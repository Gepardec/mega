import {async, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AppComponent} from './app.component';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {ConfigService} from './modules/shared/services/config/config.service';
import {UserService} from './modules/shared/services/user/user.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {Config} from './modules/shared/models/Config';
import {TranslateModule} from '@ngx-translate/core';

describe('AppComponent', () => {

  class OAuthServiceMock {

  }

  class ConfigServiceMock {
    getConfig(): Observable<Config> {
      return new BehaviorSubject<Config>({
        clientId: 'DUMMY',
        scope: 'email',
        issuer: 'https://accounts.google.com',
        version: '1',
        excelUrl: 'DUMMY',
        zepOrigin: 'DUMMY',
        budgetCalculationExcelUrl: 'DUMMY'
      });
    }
  }

  class UserServiceMock {

  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        TranslateModule.forRoot()
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        {
          provide: OAuthService, useClass: OAuthServiceMock
        },
        {
          provide: ConfigService, useClass: ConfigServiceMock
        },
        {
          provide: UserService, useClass: UserServiceMock
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });
});
