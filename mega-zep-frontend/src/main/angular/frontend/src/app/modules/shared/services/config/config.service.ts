import {Injectable} from '@angular/core';
import {Location} from "@angular/common";
import {environment} from "../../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor(private location: Location) {
  }

  getBackendUrl(): string {
    return window.location.origin.replace(environment.frontendOriginSegment, environment.backendOriginSegment);
  }
}
