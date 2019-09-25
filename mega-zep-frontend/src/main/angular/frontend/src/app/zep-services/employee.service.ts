import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {MitarbeiterResponseType} from '../models/Mitarbeiter/MitarbeiterResponseType';
import {retry} from 'rxjs/operators';
import {MitarbeiterType} from '../models/Mitarbeiter/Mitarbeiter/MitarbeiterType';
import {HttpClient} from '@angular/common/http';
import {configuration} from '../../configuration/configuration';
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

  getAll(user: SocialUser): Observable<MitarbeiterResponseType> {
    return this.http.post<MitarbeiterResponseType>(this.URL +
      '/worker/getAll/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }

  updateAll(employees: Array<MitarbeiterType>): Observable<Response> {
    return this.http.put<Response>(this.URL +
      '/worker/update/', JSON.stringify(employees))
      .pipe(
        retry(1)
      );
  }

  get(user: SocialUser): Observable<MitarbeiterType> {
    return this.http.post<MitarbeiterType>(this.URL +
      '/worker/get/', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }
}