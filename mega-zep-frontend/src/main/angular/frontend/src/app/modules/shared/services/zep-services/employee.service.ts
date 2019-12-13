import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {retry} from 'rxjs/operators';
import {Employee} from '../../models/Employee/Employee';
import {HttpClient} from '@angular/common/http';
import {configuration} from '../../constants/configuration';
import {SocialUser} from 'angularx-social-login';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private URL: string = configuration.BASEURL;

  constructor(
    private http: HttpClient
  ) {
  }

  getAll(user: SocialUser): Observable<Array<Employee>> {
    return this.http.post<Array<Employee>>(this.URL +
      '/worker/employees/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }

  updateAll(employees: Array<Employee>): Observable<Response> {
    return this.http.put<Response>(this.URL +
      '/worker/employees/update/', JSON.stringify(employees))
      .pipe(
        retry(1)
      );
  }

  get(user: SocialUser): Observable<Employee> {
    return this.http.post<Employee>(this.URL +
      '/worker/employee/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }
}
