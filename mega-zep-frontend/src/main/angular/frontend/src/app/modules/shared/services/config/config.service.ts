import {Injectable} from '@angular/core';
import {environment} from '../../../../../environments/environment';
import {Config} from '../../models/Config';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private SESSION_STORAGE_KEY = 'MEGA_CONFIG';

  constructor(private httpClient: HttpClient) {
  }

  getConfig(): Observable<Config> {
    if (localStorage.getItem(this.SESSION_STORAGE_KEY)) {
      return new BehaviorSubject(JSON.parse(localStorage.getItem(this.SESSION_STORAGE_KEY)));
    } else {
      return this.httpClient.get<Config>(this.getBackendUrlWithContext('/config'))
        .pipe(tap((result) => localStorage.setItem(this.SESSION_STORAGE_KEY, JSON.stringify(result))));
    }
  }

  logOut(): void {
    localStorage.removeItem(this.SESSION_STORAGE_KEY);
  }

  getBackendUrl(): string {
    return window.location.origin.replace(environment.frontendOriginSegment, environment.backendOriginSegment);
  }

  getBackendUrlWithContext(context: string): string {
    return this.getBackendUrl() + context;
  }
}
