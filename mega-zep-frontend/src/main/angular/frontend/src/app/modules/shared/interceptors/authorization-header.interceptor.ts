import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthStorage } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationHeaderInterceptor implements HttpInterceptor {

  constructor(
    private authStorage: OAuthStorage) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (this.authStorage.getItem('id_token')) {
      const headers = request.headers.set('X-Authorization', this.authStorage.getItem('id_token'));
      return next.handle(request.clone({headers}));
    } else {
      return next.handle(request);
    }
  }
}
