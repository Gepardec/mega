import {TestBed} from '@angular/core/testing';

import {GlobalHttpInterceptorService} from './global-http-interceptor.service';
import {ErrorHandlerService} from '../services/error/error-handler.service';
import {ConfigService} from '../services/config/config.service';
import {UserService} from '../services/user/user.service';
import {LoaderService} from '../services/loader/loader.service';
import {InfoService} from "../services/info/info.service";
import {HttpHandler} from "@angular/common/http";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Info} from "../models/Info";
import * as _moment from 'moment';

const moment = _moment;

describe('GlobalHttpInterceptorService', () => {

  let globalHttpInterceptorService: GlobalHttpInterceptorService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;
  let infoService: InfoService;
  let httpHandler: HttpHandler;
  let loaderService: LoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ErrorHandlerService, useClass: ErrorHandlerServiceMock
        },
        {
          provide: UserService, useClass: UserServiceMock
        },
        {
          provide: LoaderService, useClass: LoaderServiceMock
        }
      ]
    });

    globalHttpInterceptorService = TestBed.inject(GlobalHttpInterceptorService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
    infoService = TestBed.inject(InfoService);
    httpHandler = TestBed.inject(HttpHandler);
    loaderService = TestBed.inject(LoaderService);
  });

  it('#should be created', () => {
    expect(globalHttpInterceptorService).toBeTruthy();
  });

  it('#intercept - should return next.handle(req)', (done) => {
    spyOn(loaderService, 'showSpinner').and.callThrough();
    spyOn(loaderService, 'stopSpinner').and.callThrough();
    spyOn(httpHandler, 'handle').and.callThrough();

    infoService.getInfo()
      .subscribe(info => {
        expect(info).toBeTruthy();
        done();
      })

    const httpRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/info'));
    httpRequest.flush(InfoMock.info);
    expect(httpHandler.handle).toHaveBeenCalled();
    expect(httpRequest.request.urlWithParams).toEqual(GlobalHttpInterceptorMock.urlWithContext);
    expect(loaderService.showSpinner).not.toHaveBeenCalled();
    expect(loaderService.stopSpinner).not.toHaveBeenCalled();
  });

  class GlobalHttpInterceptorMock {

    static urlWithContext: string = 'http://localhost:9877/info';
    static url: string = 'http://localhost:9877';
  }

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

  class ErrorHandlerServiceMock {

  }

  class UserServiceMock {

  }

  class LoaderServiceMock {

    showSpinner(): void {

    }

    stopSpinner(): void {

    }
  }
});
