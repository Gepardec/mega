import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from '../config/config.service';
import { Info } from '../../models/Info';

@Injectable({
  providedIn: 'root'
})
export class InfoService {

  constructor(private httpClient: HttpClient, private config: ConfigService) {
  }

  getInfo(): Observable<Info> {
    return this.httpClient.get<Info>(this.config.getBackendUrlWithContext('/info'));
  }
}
