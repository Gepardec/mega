import {TestBed, waitForAsync} from '@angular/core/testing';

import {AuthorizationHeaderInterceptor} from './authorization-header.interceptor';
import {OAuthStorage} from 'angular-oauth2-oidc';
import {ConfigService} from '../services/config/config.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpHandler} from '@angular/common/http';
import {InfoService} from '../services/info/info.service';
import {Info} from '../models/Info';
import * as _moment from 'moment';
import {expect} from '@angular/flex-layout/_private-utils/testing';

const moment = _moment;

describe('AuthorizationHeaderInterceptor', () => {

  let component: AuthorizationHeaderInterceptor;

  let httpTestingController: HttpTestingController;
  let infoService: InfoService;
  let configService: ConfigService;
  let httpHandler: HttpHandler;
  let authStorage: OAuthStorage;

  beforeEach(waitForAsync(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthorizationHeaderInterceptor,
        OAuthStorage
      ]
    }).compileComponents().then(() => {
      component = TestBed.inject(AuthorizationHeaderInterceptor);

      httpTestingController = TestBed.inject(HttpTestingController);
      infoService = TestBed.inject(InfoService);
      configService = TestBed.inject(ConfigService);
      httpHandler = TestBed.inject(HttpHandler);
      authStorage = TestBed.inject(OAuthStorage);
    })
  ));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#intercept - should return next.handle(req)', (done) => {
    spyOn(httpHandler, 'handle').and.callThrough();

    infoService.getInfo()
      .subscribe(info => {
        expect(info).toBeTruthy();
        done();
      })

    const httpRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/info'));
    httpRequest.flush(InfoMock.info);
    expect(httpHandler.handle).toHaveBeenCalledWith(httpRequest.request);
  });

  class InfoMock {

    static info: Info = {
      branch: 'develop',
      buildDate: moment().date().toString(),
      buildNumber: 0,
      commit: '0b126bb7cf1dae67644152695918f29d96e42e6a',
      startedAt: moment().date().toString(),
      upTime: moment().fromNow().toString(),
      version: '1.0.0'
    }
  }
});


