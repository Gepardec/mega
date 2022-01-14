import {TestBed} from '@angular/core/testing';
import {ErrorHandlerService} from './error-handler.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {OAuthLogger, OAuthService, UrlHelperService} from 'angular-oauth2-oidc';
import {UserService} from '../user/user.service';
import {LoggingService} from '../logging/logging.service';
import {ErrorService} from "./error.service";
import {configuration} from "../../constants/configuration";
import {Router} from "@angular/router";

describe('ErrorHandlerService', () => {

  let errorHandlerService: ErrorHandlerService;
  let errorService: ErrorService;
  let loggingService: LoggingService;
  let userService: UserService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        ErrorHandlerService,
        OAuthService,
        UserService,
        LoggingService,
        UrlHelperService,
        OAuthLogger
      ]
    });

    errorHandlerService = TestBed.inject(ErrorHandlerService);
    errorService = TestBed.inject(ErrorService);
    loggingService = TestBed.inject(LoggingService);
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
  });

  it('#should be created', () => {
    expect(errorHandlerService).toBeTruthy();
  });

  it('#handleError - should call loggingService.writeToLog(..) and should call showErrorPage', () => {
    spyOn(errorService, 'getErrorMessage').and.callThrough();
    spyOn(loggingService, 'writeToLog').and.callThrough();

    const error: ErrrorMock = {
      status: ErrorHandlerMock.httpStatusNotFound,
      message: ErrorHandlerMock.message
    }

    errorHandlerService.handleError(error);

    expect(errorService.getErrorMessage).toHaveBeenCalledWith(error);
    expect(loggingService.writeToLog).toHaveBeenCalledWith(error.message, configuration.LogLevel.Debug);
  });

  it('#showErrorPage - should logout and navigate', () => {
    spyOn(router, 'navigate').and.stub();
    spyOn(userService, 'logoutWithoutRedirect').and.stub();
    spyOn(errorService, 'storeLastErrorData').and.callThrough();

    errorHandlerService.showErrorPage(ErrorHandlerMock.message, true);

    expect(userService.logoutWithoutRedirect).toHaveBeenCalledWith();
    expect(errorService.storeLastErrorData).toHaveBeenCalledWith(ErrorHandlerMock.message, configuration.PAGE_URLS.LOGIN);
  });

  it('#showErrorPage - should not logout but navigate', () => {
    spyOn(router, 'navigate').and.stub();
    spyOn(userService, 'logoutWithoutRedirect').and.stub();
    spyOn(errorService, 'storeLastErrorData').and.callThrough();

    errorHandlerService.showErrorPage(ErrorHandlerMock.message, false);

    expect(userService.logoutWithoutRedirect).not.toHaveBeenCalledWith();
    expect(errorService.storeLastErrorData).toHaveBeenCalledWith(ErrorHandlerMock.message, router.url);
  });

  class ErrrorMock {

    status: number;
    message: string;

  }

  class ErrorHandlerMock {

    static message: string = 'message';
    static httpStatusUnauthorized: number = 401;
    static httpStatusNotFound: number = 404;

  }
});
