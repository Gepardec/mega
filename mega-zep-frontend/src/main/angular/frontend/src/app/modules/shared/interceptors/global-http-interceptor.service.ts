import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, Observer, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ErrorHandlerService } from '../services/error/error-handler.service';
import { UserService } from '../services/user/user.service';
import { ConfigService } from '../services/config/config.service';
import { LoaderService } from '../services/loader/loader.service';
import { empty } from 'rxjs/internal/Observer';

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
    private configService: ConfigService,
    private userService: UserService,
    private loaderService: LoaderService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // apply interceptor on requests to our backend only
    if (req.url.startsWith(this.configService.getBackendUrl())) {
      this.loaderService.showSpinner();
      return next.handle(req.clone({withCredentials: true})).pipe(
        tap({complete: () => this.loaderService.stopSpinner()}),
        catchError((error: HttpErrorResponse) => {
            this.loaderService.stopSpinner();
            switch (error.status) {
              case this.HTTP_STATUS_BAD_REQUEST:
                return this.handleError(error);
              case this.HTTP_STATUS_UNAUTHORIZED:
                this.userService.logout();
                return this.handleError(error);
              case this.HTTP_STATUS_FORBIDDEN:
                this.userService.logout();
                return this.handleError(error);
              case this.HTTP_STATUS_NOT_FOUND:
                return this.handleError(error);
              case this.HTTP_STATUS_METHOD_NOT_ALLOWED:
                return this.handleError(error);
              case this.HTTP_STATUS_REQUEST_TIMEOUT:
                return this.handleError(error);
              default:
                return throwError(error);
            }
          }
        )
      );
    } else {
      return next.handle(req);
    }
  }

  private handleError(error): Observable<any> {
    this.errorHandler.handleError(error);
    return of(error);
  }
}
