import {Injectable} from '@angular/core';
import {environment} from "../../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor() {
  }

  getBackendUrl(): string {
    return window.location.origin.replace(environment.frontendOriginSegment, environment.backendOriginSegment);
  }
}
