import {Injectable} from '@angular/core';
import {ConfigService} from '../../shared/services/config/config.service';
import {Employee} from '../../shared/models/Employee';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {ManagementEntry} from '../../shared/models/ManagementEntry';
import * as _moment from 'moment';

const moment = _moment;

@Injectable({
  providedIn: 'root'
})
export class OfficeManagementService {

  selectedYear = new BehaviorSubject<number>(moment().subtract(1, 'month').year());
  selectedMonth = new BehaviorSubject<number>(moment().subtract(1, 'month').month() + 1);

  constructor(private configService: ConfigService,
              private httpClient: HttpClient) {
  }

  getEntries(year: number, month: number): Observable<Array<ManagementEntry>> {
    return this.httpClient.get<Array<ManagementEntry>>(
      this.configService.getBackendUrlWithContext('/management/officemanagemententries/' + year + '/' + month));
  }

  updateEmployees(employees: Array<Employee>): Observable<Response> {
    return this.httpClient.put<Response>(this.configService.getBackendUrlWithContext('/employees'), employees);
  }
}
