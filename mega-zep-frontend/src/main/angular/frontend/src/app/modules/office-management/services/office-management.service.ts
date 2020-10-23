import { Injectable } from '@angular/core';
import { ConfigService } from '../../shared/services/config/config.service';
import { Employee } from '../../shared/models/Employee';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OfficeManagementService {

  constructor(private configService: ConfigService, private httpClient: HttpClient) {
  }

  getEmployees(): Observable<Array<Employee>> {
    return this.httpClient.get<Array<Employee>>(this.configService.getBackendUrlWithContext('/employees'));
  }

  updateEmployees(employees: Array<Employee>): Observable<Response> {
    return this.httpClient.put<Response>(this.configService.getBackendUrlWithContext('/employees'), employees);
  }
}
