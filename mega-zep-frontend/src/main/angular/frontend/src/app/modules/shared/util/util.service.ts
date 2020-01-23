import {Injectable} from '@angular/core';
import {EmployeeService} from "../../employees/services/employee.service";
import {Employee} from "../../employees/models/Employee";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  updateEmployees(employees: Array<Employee>): Observable<Response> {
    let response = this.employeeService.updateAll(employees);
    return response;
  }
}
