import {TestBed} from '@angular/core/testing';

import {ConfigService} from './config.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Config} from '../../models/Config';
import {LocalStorageService} from '../local-storage/local-storage.service';

describe('ConfigService', () => {

  let configService: ConfigService;
  let httpTestingController: HttpTestingController;

  let localStorageService: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });

    configService = TestBed.inject(ConfigService);
    httpTestingController = TestBed.inject(HttpTestingController);
    localStorageService = TestBed.inject(LocalStorageService);

    localStorageService.removeConfig();
  });

  it('#should be created', () => {
    expect(configService).toBeTruthy();
  });

  it('#getConfig - should do http call and return config and set in localStorage', (done) => {
    configService.getConfig()
      .subscribe(config => {
        expect(config).toEqual(ConfigMock.config);
        expect(localStorageService.getConfig()).toEqual(config);

        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/config'));
    testRequest.flush(ConfigMock.config);
  });

  it('#getConfig - should return config without http call', (done) => {
    localStorageService.saveConfig(ConfigMock.config);

    configService.getConfig()
      .subscribe(config => {
        expect(config).toEqual(ConfigMock.config);
        expect(localStorageService.getConfig()).toEqual(config);

        done();
      });

    httpTestingController.expectNone(configService.getBackendUrlWithContext('/config'));
  });

  it('#logOut - should remove localStorage item', () => {
    localStorageService.saveConfig(ConfigMock.config);

    expect(localStorageService.getConfig()).toBeTruthy();

    configService.logOut();
    expect(localStorageService.getConfig()).not.toBeTruthy();
  });

  it('#getBackendUrl - should return backend url', () => {
    expect(configService.getBackendUrl()).toBeTruthy();
  });

  it('#getBackendUrlWithContext - should return backend url with context', () => {
    expect(configService.getBackendUrlWithContext(ConfigMock.context)).toContain(ConfigMock.context);
  });

  class ConfigMock {

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
