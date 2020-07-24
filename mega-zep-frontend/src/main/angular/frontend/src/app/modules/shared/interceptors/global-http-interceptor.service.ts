import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ErrorHandlerService } from '../services/error/error-handler.service';
import { UserService } from '../services/user/user.service';
import { ConfigService } from '../services/config/config.service';
import { LoaderService } from '../services/loader/loader.service';
import { OAuthStorage } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class GlobalHttpInterceptorService implements HttpInterceptor {

  constructor(
    private errorHandler: ErrorHandlerService,
    private configService: ConfigService,
    private userService: UserService,
    private loaderService: LoaderService,
    private authStorage: OAuthStorage) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // apply interceptor on requests to our backend only
    if (req.url.startsWith(this.configService.getBackendUrl())) {
      this.loaderService.showSpinner();

      let headers;
      if (this.authStorage.getItem('id_token')) {
        headers = req.headers.set('X-Authorization', this.authStorage.getItem('id_token'));
      } else {
        headers = req.headers;
      }

      return next.handle(req.clone({withCredentials: true, headers})).pipe(
        tap({
          complete: () =>
            this.loaderService.stopSpinner()
        }),

        catchError((error: HttpErrorResponse) => {
            this.loaderService.stopSpinner();
            // TODO: we should lookup documentation on this code, not necessary a problem with this pull request ... is the direct call of handleError from application code valid?
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
