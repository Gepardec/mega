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

  constructor(private injector: Injector) {

  }

  handleError(error: any): void {
    const errorService = this.injector.get(ErrorService);
    const logger = this.injector.get(LoggingService);

    let message = errorService.getErrorMessage(error);
    logger.writeToLog(message, configuration.LogLevel.Debug);

    if(error.status === this.HTTP_STATUS_UNAUTHORIZED || error.status === this.HTTP_STATUS_FORBIDDEN) {
      this.showErrorPageAndLogout(message);
    } else {
      this.showErrorPage(message);
    }
  }

  showErrorPageAndLogout(message: string) {
    const router = this.injector.get(Router);
    const zone = this.injector.get(NgZone);
    const userService = this.injector.get(UserService);
    userService.logoutWithoutRedirect();
    zone.run(() => router.navigate([configuration.PAGE_URLS.ERROR, {errorMessage: message, previousPage: configuration.PAGE_URLS.LOGIN}]));
  }

  showErrorPage(message: string) {
    const router = this.injector.get(Router);
    const zone = this.injector.get(NgZone);
    let previousPage = router.url;
    zone.run(() => router.navigate([configuration.PAGE_URLS.ERROR, {errorMessage: message, previousPage: previousPage}]));
  }
}
