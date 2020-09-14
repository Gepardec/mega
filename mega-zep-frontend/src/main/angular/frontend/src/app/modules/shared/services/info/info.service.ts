import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from '../config/config.service';
import { Info } from '../../models/Info';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class InfoService {

  readonly SESSION_STORAGE_KEY = 'MEGA_INFO';

  constructor(private httpClient: HttpClient, private config: ConfigService) {
  }

  getInfo(): Observable<Info> {
    const megaInfo = sessionStorage.getItem(this.SESSION_STORAGE_KEY);
    return megaInfo ?
      new BehaviorSubject(JSON.parse(megaInfo)) :
      this.httpClient.get<Info>(this.config.getBackendUrlWithContext('/info'))
        .pipe(tap(info => sessionStorage.setItem(this.SESSION_STORAGE_KEY, JSON.stringify(info))));
  }
}
