import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {retry} from 'rxjs/operators';
import {Employee} from '../../models/Employee/Employee';
import {HttpClient} from '@angular/common/http';
import {environment} from "../../../../../environments/environment";
import {configuration} from '../../constants/configuration';
import {SocialUser} from 'angularx-social-login';
import {ConfigService} from "../config/config.service";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(
    private http: HttpClient,
    private config: ConfigService
  ) {
  }

  getAll(): Observable<Array<Employee>> {
    return this.http.post<Array<Employee>>(this.config.getBackendUrl() +
      '/worker/employees/', null)
      .pipe(
        retry(1)
      );
  }

  updateAll(employees: Array<Employee>): Observable<Response> {
    return this.http.put<Response>(this.config.getBackendUrl() +
      '/worker/employees/update/', JSON.stringify(employees))
      .pipe(
        retry(1)
      );
  }

  get(user: SocialUser): Observable<Employee> {
    return this.http.post<Employee>(this.config.getBackendUrl() +
      '/worker/employee/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }
}
