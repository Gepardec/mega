import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AppComponent} from './app.component';
import {OAuthModule, OAuthService} from 'angular-oauth2-oidc';
import {ConfigService} from './modules/shared/services/config/config.service';
import {UserService} from './modules/shared/services/user/user.service';
import {BehaviorSubject, Observable} from 'rxjs';
import {Config} from './modules/shared/models/Config';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {expect} from "@angular/flex-layout/_private-utils/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {NO_ERRORS_SCHEMA} from "@angular/core";

const LANG_DE = 'de';

describe('AppComponent', () => {

  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;

  let translateService: TranslateService;
  let authService: OAuthService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        RouterTestingModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        NoopAnimationsModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ],
      providers: [
        {provide: ConfigService, useClass: ConfigServiceMock},
        {provide: UserService, useClass: UserServiceMock}
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(AppComponent);
      component = fixture.componentInstance;
      translateService = TestBed.inject(TranslateService);
      authService = TestBed.inject(OAuthService);
    });
  }));

  it('#should create', fakeAsync(() => {
    expect(component).toBeTruthy();
  }));

  it('#afterInit - should set default language and add "de" language', () => {
    expect(translateService.defaultLang).toEqual(LANG_DE);
    expect(translateService.getLangs()).toContain(LANG_DE);
    expect(translateService.get(LANG_DE)).toBeTruthy();
  });

  it("#afterInit - should call authService.configure and should call oAuthService.loadDiscoveryDocumentAndTryLogin", fakeAsync(() => {
    spyOn(authService, 'configure').and.stub();
    spyOn(authService, 'loadDiscoveryDocumentAndTryLogin').and.returnValue(new Promise(() => Promise.resolve()));

    fixture.detectChanges();
    flush();

    expect(authService.configure).toHaveBeenCalled();
    expect(authService.loadDiscoveryDocumentAndTryLogin).toHaveBeenCalled();
  }));

  it("#onDestroy - should close subscription", fakeAsync(() => {
    spyOn(authService, 'configure').and.stub();
    spyOn(authService, 'loadDiscoveryDocumentAndTryLogin').and.returnValue(new Promise(() => Promise.resolve()));

    fixture.detectChanges();
    flush();

    expect((component as any).configServiceSubscription).toBeTruthy();
    expect((component as any).configServiceSubscription.closed).toBeFalse();
    component.ngOnDestroy();

    expect((component as any).configServiceSubscription.closed).toBeTrue();
  }));

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
});
