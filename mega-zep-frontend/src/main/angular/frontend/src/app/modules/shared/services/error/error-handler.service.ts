import {ErrorHandler, Injectable, NgZone} from '@angular/core';
import {LoggingService} from '../logging/logging.service';
import {ErrorService} from './error.service';
import {configuration} from '../../constants/configuration';
import {Router} from '@angular/router';
import {UserService} from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  private readonly HTTP_STATUS_UNAUTHORIZED: number = 401;
  private readonly HTTP_STATUS_FORBIDDEN: number = 403;

  constructor(
    private errorService: ErrorService,
    private router: Router,
    private userService: UserService,
    private loggingService: LoggingService,
    private ngZone: NgZone
  ) {
  }

  handleError(error: any): void {
    const message = this.errorService.getErrorMessage(error);
    this.loggingService.writeToLog(message, configuration.LogLevel.Debug);

    const logout = error.status === this.HTTP_STATUS_UNAUTHORIZED || error.status === this.HTTP_STATUS_FORBIDDEN;

    this.showErrorPage(message, logout);
  }

  showErrorPage(message: string, logout: boolean) {
    let redirectUrl;

    if (logout) {
      redirectUrl = configuration.PAGE_URLS.LOGIN;

      this.userService.logoutWithoutRedirect();
    } else {
      redirectUrl = this.router.url;
    }

    this.errorService.storeLastErrorData(message, redirectUrl);

    // TODO: use of zone is dangerous and should be avoided
    //  as mentioned above we should move the router to error-service to solve cyclic dependency
    this.ngZone.run(() => this.router.navigate([configuration.PAGE_URLS.ERROR]));
  }
}
