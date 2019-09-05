// httpSetHeaders.interceptor.ts
import {Injectable} from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';

import {Observable, throwError} from 'rxjs';
import {catchError, map} from "rxjs/operators";
import {configuration} from "../../../configuration/configuration";
import {AuthenticationService} from "../../signin/authentication.service";

@Injectable()
export class ErrorHandleInterceptor implements HttpInterceptor {

  private readonly httpOptions = configuration.httpOptions;

  constructor(
    private authenticationService: AuthenticationService
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    request = request.clone(this.httpOptions);

    return next.handle(request).pipe(
      map((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          console.log('event', event);
        }
        return event;
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 403) {
          this.authenticationService.logout();
        }
        return throwError(error);
      }));
  }
}
