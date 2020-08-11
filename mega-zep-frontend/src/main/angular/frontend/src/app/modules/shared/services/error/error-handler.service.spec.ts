import { TestBed } from '@angular/core/testing';
import { ErrorHandlerService } from './error-handler.service';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OAuthLogger, OAuthService, UrlHelperService } from 'angular-oauth2-oidc';
import { UserService } from '../user/user.service';
import { LoggingService } from '../logging/logging.service';

describe('ErrorHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule, RouterTestingModule],
    providers: [
      ErrorHandlerService,
      OAuthService,
      UserService,
      LoggingService,
      UrlHelperService,
      OAuthLogger
    ]
  }));

  it('should be created', () => {
    const service: ErrorHandlerService = TestBed.get(ErrorHandlerService);
    expect(service).toBeTruthy();
  });
});
