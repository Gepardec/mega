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
    /*Due to the mannerism of moment.js, the month ranges from 0 (=January) to 11 (=December)
    * Since the range of the month passed to the method is between 1 and 12, the given month has to be manipulated
    * manually (month - 1). This ensures that moment.js returns us the correct new year/month.
    * The month value returned by moment has to be re-translated into our date range, obviously (1 + ...).*/
    const newYear = moment().year(year).month(month - 1).add(1, 'month').year();
    const newMonth = 1 + moment().year(year).month(month - 1).add(1, 'month').month();

    return this.httpClient.get<MonthlyReport>(this.config.getBackendUrlWithContext(`/worker/monthendreports/${newYear}/${newMonth}`));
  }
}
