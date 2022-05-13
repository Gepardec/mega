import {TestBed} from '@angular/core/testing';

import {LoggingService} from './logging.service';
import {configuration} from '../../constants/configuration';

describe('LoggingService', () => {

  let loggingService: LoggingService;

  beforeEach(() => {
    loggingService = TestBed.inject(LoggingService);
  });

  it('#should be created', () => {
    expect(loggingService).toBeTruthy();
  });

  it('#writeToLog - should call console.log', () => {
    spyOn(console, 'log').and.callThrough();

    loggingService.writeToLog(LoggingMock.message, LoggingMock.levelDebug);

    expect(console.log).toHaveBeenCalled();
  });

  it('#writeToLog - should not call console.log', () => {
    spyOn(console, 'log').and.callThrough();

    loggingService.writeToLog(LoggingMock.message, LoggingMock.levelOff);

    expect(console.log).not.toHaveBeenCalled();
  });

  class LoggingMock {

    static message: string = 'Hello World';
    static levelDebug: number = configuration.LogLevel.Debug;
    static levelOff: number = configuration.LogLevel.Off;

  }
});
