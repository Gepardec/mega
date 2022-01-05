import {TestBed} from '@angular/core/testing';

import {ErrorService} from './error.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {HttpErrorResponse} from "@angular/common/http";

describe('ErrorService', () => {

  let errorService: ErrorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    errorService = TestBed.inject(ErrorService);
  });

  it('#should be created', () => {
    expect(errorService).toBeTruthy();
  });

  it('#storeLastErrorData - should store last error data', () => {
    errorService.storeLastErrorData(ErrorMock.message, ErrorMock.redirectUrl);

    expect(errorService.message).toEqual(ErrorMock.message);
    expect(errorService.redirectUrl).toEqual(ErrorMock.redirectUrl);
  });

  it('#removeLastErrorData - should delete last error data', () => {
    errorService.storeLastErrorData(ErrorMock.message, ErrorMock.redirectUrl);
    expect(errorService.message).toEqual(ErrorMock.message);

    errorService.removeLastErrorData();

    expect(errorService.message).toBeUndefined();
  });

  it('#getErrorMessage - should get server message', () => {
    errorService.storeLastErrorData(ErrorMock.message, ErrorMock.redirectUrl);
    expect(errorService.message).toEqual(ErrorMock.message);

    const error = new HttpErrorResponse({statusText: ErrorMock.message});
    const errorMessage = errorService.getErrorMessage(error);

    expect(errorMessage).toContain(ErrorMock.message);
  });

  it('#getErrorMessage - should get client message', () => {
    const error = new ClientErrorMessage(ErrorMock.message);
    const errorMessage = errorService.getErrorMessage(error);

    expect(errorMessage).toEqual(ErrorMock.message);
  });

  it('#getClientMessage - should return message', () => {
    const error = new ClientErrorMessage(ErrorMock.message);
    const errorMessage = errorService.getClientMessage(error);

    expect(errorMessage).toEqual(ErrorMock.message);
  });

  it('#getClientMessage - should return Error.toString()', () => {
    const error = new ClientErrorMessage();
    const errorMessage = errorService.getClientMessage(error);

    expect(errorMessage).toEqual('Error');
  });

  it('#getClientStack - should return Error.stack', () => {
    const error = new ClientErrorMessage();
    const errorMessage = errorService.getClientStack(error);

    expect(errorMessage).toEqual(error.stack);
  });

  it('#getClientStack - should return Error.stack', () => {
    const error = new ClientErrorMessage();
    error.stack = ErrorMock.stack;
    const errorMessage = errorService.getClientStack(error);

    expect(errorMessage).toEqual(ErrorMock.stack);
  });

  it('#getServerMessage - should return error.message', () => {
    const error = new HttpErrorResponse({statusText: ErrorMock.message});
    const errorMessage = errorService.getServerMessage(error);

    expect(errorMessage).toContain(ErrorMock.message);
  });

  it('#getServerStack - should return error.error', () => {
    const error = new HttpErrorResponse({statusText: ErrorMock.stack});
    const errorMessage = errorService.getServerMessage(error);

    expect(errorMessage).toContain(ErrorMock.stack);
  });

  class ErrorMock {

    static message: string = 'error message';
    static redirectUrl: string = 'redirectUrl';
    static stack: string = 'stack';
  }

  class ClientErrorMessage extends Error {
    message: string;
    stack: string;
  }
});
