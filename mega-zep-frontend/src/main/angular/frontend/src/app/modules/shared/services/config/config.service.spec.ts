import {TestBed} from '@angular/core/testing';

import {ConfigService} from './config.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Config} from "../../models/Config";

describe('ConfigService', () => {

  let configService: ConfigService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    configService = TestBed.inject(ConfigService);
    httpTestingController = TestBed.inject(HttpTestingController);

    sessionStorage.removeItem(ConfigMock.sessionStorageKey);
  });

  it('#should be created', () => {
    expect(configService).toBeTruthy();
  });

  it('#getConfig - should do http call and return config and set in sessionStorage', (done) => {
    configService.getConfig()
      .subscribe(config => {
        expect(config).toEqual(ConfigMock.config);
        expect(sessionStorage.getItem(ConfigMock.sessionStorageKey)).toEqual(JSON.stringify(config));

        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/config'));
    testRequest.flush(ConfigMock.config);
  });

  it('#getConfig - should return config without http call', (done) => {
    sessionStorage.setItem(ConfigMock.sessionStorageKey, JSON.stringify(ConfigMock.config));

    configService.getConfig()
      .subscribe(config => {
        expect(config).toEqual(ConfigMock.config);
        expect(sessionStorage.getItem(ConfigMock.sessionStorageKey)).toEqual(JSON.stringify(config));

        done();
      });

    httpTestingController.expectNone(configService.getBackendUrlWithContext('/config'));
  });

  it('#logOut - should remove sessionStorage item', () => {
    sessionStorage.setItem(ConfigMock.sessionStorageKey, JSON.stringify(ConfigMock.config));
    expect(sessionStorage.getItem(ConfigMock.sessionStorageKey)).toBeTruthy();

    configService.logOut();
    expect(sessionStorage.getItem(ConfigMock.sessionStorageKey)).not.toBeTruthy();
  });

  it('#getBackendUrl - should return backend url', () => {
    expect(configService.getBackendUrl()).toBeTruthy();
  });

  it('#getBackendUrlWithContext - should return backend url with context', () => {
    expect(configService.getBackendUrlWithContext(ConfigMock.context)).toContain(ConfigMock.context);
  });

  class ConfigMock {

    static sessionStorageKey: string = 'MEGA_CONFIG';
    static frontendOriginSegment: number = 9876;
    static context: string = '/context/employee'

    static config: Config = {
      budgetCalculationExcelUrl: 'budgetCalculationExcelUrl',
      clientId: 'clientId',
      excelUrl: 'excelUrl',
      issuer: 'issuer',
      scope: 'scope',
      version: 'version',
      zepOrigin: 'zepOrigin'
    }
  }
});
