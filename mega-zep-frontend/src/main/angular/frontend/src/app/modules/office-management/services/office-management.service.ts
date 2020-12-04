import {Injectable} from '@angular/core';
import {ConfigService} from '../../shared/services/config/config.service';
import {Employee} from '../../shared/models/Employee';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ManagementEntry} from '../../shared/models/ManagementEntry';


@Injectable({
  providedIn: 'root'
})
export class OfficeManagementService {

  constructor(private configService: ConfigService, private httpClient: HttpClient) {
  }

  getEntries(year: number, month: number): Observable<Array<ManagementEntry>> {
    return this.httpClient.get<Array<ManagementEntry>>(
      this.configService.getBackendUrlWithContext('/management/officemanagemententries/' + year + '/' + month));
  }

  updateEmployees(employees: Array<Employee>): Observable<Response> {
    return this.httpClient.put<Response>(this.configService.getBackendUrlWithContext('/employees'), employees);
  }
}
