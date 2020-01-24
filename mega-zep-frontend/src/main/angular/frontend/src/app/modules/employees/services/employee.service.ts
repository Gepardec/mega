import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../models/Employee';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from '../../shared/services/config/config.service';
import { User } from '../../shared/models/User';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient,
              private config: ConfigService) {
  }

  getAll(): Observable<Array<Employee>> {
    return this.httpClient.post<Array<Employee>>(this.config.getBackendUrl() +
      '/worker/employees/', null);
  }

  updateAll(employees: Array<Employee>): Observable<Response> {
    return this.httpClient.put<Response>(this.config.getBackendUrl() +
      '/worker/employees/update/', employees);

  }

  get(user: User): Observable<Employee> {
    return this.httpClient.post<Employee>(this.config.getBackendUrl() +
      '/worker/employee/', user);
  }
}
