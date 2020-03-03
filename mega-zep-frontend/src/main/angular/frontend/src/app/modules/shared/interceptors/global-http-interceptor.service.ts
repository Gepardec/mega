import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ErrorHandlerService } from '../services/error/error-handler.service';
import { UserService } from '../services/user/user.service';
import { ConfigService } from '../services/config/config.service';
import { LoaderService } from '../services/loader/loader.service';

@Injectable({
  providedIn: 'root'
})
export class GlobalHttpInterceptorService implements HttpInterceptor {

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
            return this.handleError(error);
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
