import {TestBed} from '@angular/core/testing';

import {GlobalHttpInterceptorService} from './global-http-interceptor.service';
import {ErrorHandlerService} from '../services/error/error-handler.service';
import {ConfigService} from '../services/config/config.service';
import {UserService} from '../services/user/user.service';
import {LoaderService} from '../services/loader/loader.service';

describe('GlobalHttpInterceptorService', () => {

  class ErrorHandlerServiceMock {

  }

  class ConfigServiceMock {

  }

  class UserServiceMock {

  }

  class LoaderServiceMock {

  }

  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {
        provide: ErrorHandlerService, useClass: ErrorHandlerServiceMock
      },
      {
        provide: ConfigService, useClass: ConfigServiceMock
      },
      {
        provide: UserService, useClass: UserServiceMock
      },
      {
        provide: LoaderService, useClass: LoaderServiceMock
      }
    ]
  }));

  it('should be created', () => {
    const service: GlobalHttpInterceptorService = TestBed.get(GlobalHttpInterceptorService);
    expect(service).toBeTruthy();
  });
});
