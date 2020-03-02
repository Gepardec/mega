import {Injectable, Injector, ErrorHandler, NgZone} from '@angular/core';
import {LoggingService} from '../logging/logging.service';
import {NotificationService} from '../notification/notification.service';
import {ErrorService} from './error.service';
import {HttpErrorResponse} from '@angular/common/http';
import {configuration} from '../../constants/configuration';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
// FIXME GAJ: remove NotificationService if no longer in use
export class ErrorHandlerService implements ErrorHandler {

  constructor(private injector: Injector) {

  }

  // FIXME GAJ: redirect with logout doesnt work as expected
  handleError(error: any): void {
    const errorService = this.injector.get(ErrorService);
    const logger = this.injector.get(LoggingService);
    // const notifier = this.injector.get(NotificationService);

    let message;

    if (error instanceof HttpErrorResponse) {
      // Server Error
      message = errorService.getServerMessage(error);
      // notifier.showError(message);
    } else {
      // Client Error
      message = errorService.getClientMessage(error);
      // notifier.showError(message);
    }

    this.showErrorPage(message);
    logger.writeToLog(message, configuration.LogLevel.Debug);
  }

  showErrorPage(message: string) {
    const router = this.injector.get(Router);
    const zone = this.injector.get(NgZone);
    let previousPage = router.url;
    zone.run(() => router.navigate([configuration.PAGE_URLS.ERROR, {errorMessage: message, previousPage: previousPage}]));
  }
}
