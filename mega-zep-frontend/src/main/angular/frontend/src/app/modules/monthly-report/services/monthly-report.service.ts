import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MonthlyReport} from '../models/MonthlyReport';
import {BehaviorSubject, Observable} from 'rxjs';
import {ConfigService} from '../../shared/services/config/config.service';
import * as _moment from 'moment';

const moment = _moment;

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportService {

  monthlyReport: MonthlyReport;

  selectedYear = new BehaviorSubject<number>(moment().subtract(1, 'month').year());
  selectedMonth = new BehaviorSubject<number>(moment().subtract(1, 'month').month() + 1);

  billablePercentage: number;
  totalWorkingTimeHours: number;
  billableTimeHours: number;

  constructor(
    private httpClient: HttpClient,
    private config: ConfigService) {
  }

  getAll(): Observable<MonthlyReport> {
    return this.httpClient.get<MonthlyReport>(this.config.getBackendUrlWithContext('/worker/monthendreports'));
  }

  getAllByDate(year: number, month: number): Observable<MonthlyReport> {
    return this.httpClient.get<MonthlyReport>(this.config.getBackendUrlWithContext(`/worker/monthendreports/${year}/${month + 1}`));
  }
}
