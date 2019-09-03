import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {retry} from 'rxjs/operators';
import {configuration} from '../../../configuration/configuration';
import {MitarbeiterType} from '../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType';
import {EmployeeService} from "../../zep-services/employee.service";
import {SocialUser} from "angularx-social-login";

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  URL: string = configuration.BASEURL;

  constructor(
    private http: HttpClient,
    private employeeService: EmployeeService
  ) {
  }

  getEmployee(user: SocialUser): Observable<MitarbeiterType> {
    return this.employeeService.get(user);
  }
}
