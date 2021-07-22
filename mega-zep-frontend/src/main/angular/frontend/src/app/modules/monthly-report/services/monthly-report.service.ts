import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MonthlyReport} from '../models/MonthlyReport';
import {Observable} from 'rxjs';
import {ConfigService} from '../../shared/services/config/config.service';

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportService {

  constructor(
    private httpClient: HttpClient,
    private config: ConfigService) {
  }

  getAll(): Observable<MonthlyReport> {
    return this.httpClient.get<MonthlyReport>(this.config.getBackendUrlWithContext('/worker/monthendreports'));
  }
}
