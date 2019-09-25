import { Injectable } from '@angular/core';
import {configuration} from "../../../configuration/configuration";
import {HttpClient} from "@angular/common/http";
import {EmployeeService} from "../../zep-services/employee.service";
import {SocialUser} from "angularx-social-login";
import {Observable} from "rxjs";
import {MitarbeiterType} from "../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  getEmployee(user: SocialUser): Observable<MitarbeiterType> {
    return this.employeeService.get(user);
  }
}
