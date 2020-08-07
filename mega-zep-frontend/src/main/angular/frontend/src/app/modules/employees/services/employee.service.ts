import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../models/Employee';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from '../../shared/services/config/config.service';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient,
              private config: ConfigService) {
  }

  getAll(): Observable<Array<Employee>> {
    return this.httpClient.get<Array<Employee>>(this.config.getBackendUrlWithContext('/employees'));
  }

  updateAll(employees: Array<Employee>): Observable<Response> {
    return this.httpClient.put<Response>(this.config.getBackendUrlWithContext('/employees'), employees);
  }
}
