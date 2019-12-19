import { Injectable } from '@angular/core';
import {NotificationService} from "../services/notification/notification.service";
import {ZepSigninService} from "../services/signin/zep-signin.service";
import {LoggingService} from "../services/logging/logging.service";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {configuration} from "../constants/configuration";
import {catchError} from "rxjs/operators";
import {ErrorHandlerService} from "../services/error/error-handler.service";

@Injectable({
  providedIn: 'root'
})
// TODO: Snackbar opens multiple times because of retry-mechanism of rest calls
// possible solution: handle error after retries -> implement logic in service where calls are executed
export class GlobalHttpInterceptorService implements HttpInterceptor {

  private readonly HTTP_STATUS_BAD_REQUEST: number = 400;
  private readonly HTTP_STATUS_UNAUTHORIZED: number = 401;
  private readonly HTTP_STATUS_FORBIDDEN: number = 403;
  private readonly HTTP_STATUS_NOT_FOUND: number = 404;
  private readonly HTTP_STATUS_METHOD_NOT_ALLOWED: number = 405;
  private readonly HTTP_STATUS_REQUEST_TIMEOUT: number = 408;

  constructor(
    private errorHandler: ErrorHandlerService,
    private authenticationService: ZepSigninService
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const modified = req.clone(configuration.httpOptions);


    return next.handle(modified).pipe(
      catchError((error: HttpErrorResponse) => {
          switch (error.status) {
            case this.HTTP_STATUS_BAD_REQUEST:
              this.errorHandler.handleError(error);
              break;
            case this.HTTP_STATUS_UNAUTHORIZED:
              this.errorHandler.handleError(error);
              this.authenticationService.logout();
              break;
            case this.HTTP_STATUS_FORBIDDEN:
              this.errorHandler.handleError(error);
              this.authenticationService.logout();
              break;
            case this.HTTP_STATUS_NOT_FOUND:
              this.errorHandler.handleError(error);
              break;
            case this.HTTP_STATUS_METHOD_NOT_ALLOWED:
              this.errorHandler.handleError(error);
              break;
            case this.HTTP_STATUS_REQUEST_TIMEOUT:
              this.errorHandler.handleError(error);
              break;
            default:
              return throwError(error);
          }
        }
      )
    );
  }
}
