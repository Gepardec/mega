import {TestBed} from '@angular/core/testing';

import {InfoService} from './info.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ConfigService} from "../config/config.service";
import {Info} from "../../models/Info";
import * as _moment from 'moment';

const moment = _moment;

describe('InfoService', () => {

  let infoService: InfoService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    infoService = TestBed.inject(InfoService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(infoService).toBeTruthy();
  });

  it('#getInfo - should return info and do http call', (done) => {
    infoService.getInfo()
      .subscribe(info => {
        expect(info).toEqual(InfoMock.info);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/info'));
    testRequest.flush(InfoMock.info);
  });

  it('#getInfo - should return info twice and do http call only once', (done) => {
    infoService.getInfo()
      .subscribe(info => {
        expect(info).toEqual(InfoMock.info);

        infoService.getInfo()
          .subscribe(info => {
            expect(info).toEqual(InfoMock.info);
            done();
          });
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/info'));
    testRequest.flush(InfoMock.info);
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
