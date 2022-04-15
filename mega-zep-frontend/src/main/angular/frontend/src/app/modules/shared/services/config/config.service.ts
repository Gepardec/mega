import {Injectable} from '@angular/core';
import {environment} from '../../../../../environments/environment';
import {Config} from '../../models/Config';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {LocalStorageService} from '../local-storage/local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor(private httpClient: HttpClient,
              private localStorageService: LocalStorageService) {
  }

  getConfig(): Observable<Config> {
    const config = this.localStorageService.getConfig();

    if (config) {
      return new BehaviorSubject(config);
    } else {
      return this.httpClient.get<Config>(this.getBackendUrlWithContext('/config'))
        .pipe(tap(resultConfig => this.localStorageService.saveConfig(resultConfig)));
    }
  }

  logOut(): void {
    this.localStorageService.removeConfig();
  }

  getBackendUrl(): string {
    return window.location.origin.replace(environment.frontendOriginSegment, environment.backendOriginSegment);
  }

  getBackendUrlWithContext(context: string): string {
    return this.getBackendUrl() + context;
  }
}
