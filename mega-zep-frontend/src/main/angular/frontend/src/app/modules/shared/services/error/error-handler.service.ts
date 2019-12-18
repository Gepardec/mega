import {Injectable, Injector, ErrorHandler} from '@angular/core';
import {LoggingService} from "../logging/logging.service";
import {NotificationService} from "../notification/notification.service";
import {ErrorService} from "./error.service";
import {HttpErrorResponse} from "@angular/common/http";
import {configuration} from "../../constants/configuration";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  constructor(private injector: Injector) {

  }

  handleError(error: any): void {
    const errorService = this.injector.get(ErrorService);
    const logger = this.injector.get(LoggingService);
    const notifier = this.injector.get(NotificationService);

    let message;
    let stackTrace;

    if (error instanceof HttpErrorResponse) {
      // Server Error
      message = errorService.getServerMessage(error);
      notifier.showError(message);
    } else {
      // Client Error
      message = errorService.getClientMessage(error);
      notifier.showError(message);
    }

    logger.writeToLog(message, configuration.LogLevel.Debug);

  }
}
