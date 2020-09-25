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

  private megaInfo: Info;

  constructor(private httpClient: HttpClient, private config: ConfigService) {
  }

  getInfo(): Observable<Info> {
    if (this.megaInfo) {
      return new BehaviorSubject(this.megaInfo);
    } else {
      return this.httpClient.get<Info>(this.config.getBackendUrlWithContext('/info'))
        .pipe(tap(info => this.megaInfo = info));
    }
  }
}
