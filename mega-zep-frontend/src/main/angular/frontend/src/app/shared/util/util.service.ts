import {Injectable} from '@angular/core';
import {EmployeeService} from "../../zep-services/employee.service";
import {MitarbeiterType} from "../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  updateEmployees(employees: Array<MitarbeiterType>): Observable<Response> {
    let response = this.employeeService.updateAll(employees);
    return response;
  }
}
