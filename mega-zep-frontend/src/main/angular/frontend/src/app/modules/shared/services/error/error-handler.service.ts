import {Injectable, Injector, ErrorHandler, NgZone} from '@angular/core';
import {LoggingService} from '../logging/logging.service';
import {ErrorService} from './error.service';
import {configuration} from '../../constants/configuration';
import {Router} from "@angular/router";
import {UserService} from "../user/user.service";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  private readonly HTTP_STATUS_UNAUTHORIZED: number = 401;
  private readonly HTTP_STATUS_FORBIDDEN: number = 403;

  errorService: ErrorService;

  constructor(private injector: Injector) {
    this.errorService = this.injector.get(ErrorService);
  }

  handleError(error: any): void {
    const logger = this.injector.get(LoggingService);

    let message = this.errorService.getErrorMessage(error);
    logger.writeToLog(message, configuration.LogLevel.Debug);

    let logout = error.status === this.HTTP_STATUS_UNAUTHORIZED || error.status === this.HTTP_STATUS_FORBIDDEN;

    this.showErrorPage(message, logout);
  }

  showErrorPage(message: string, logout: boolean) {
    const router = this.injector.get(Router);
    const zone = this.injector.get(NgZone);

    let redirectPage;
    if (logout) {
      const userService = this.injector.get(UserService);
      userService.logoutWithoutRedirect();
      redirectPage = configuration.PAGE_URLS.LOGIN;
    } else {
      redirectPage = router.url;
    }

    this.errorService.storeLastErrorData(message, redirectPage);
    zone.run(() => router.navigate([configuration.PAGE_URLS.ERROR]));
  }
}
