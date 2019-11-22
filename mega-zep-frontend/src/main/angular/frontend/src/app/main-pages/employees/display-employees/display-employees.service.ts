import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {SelectionChange} from "@angular/cdk/collections";
import {Employee} from "../../../models/Employee/Employee";
import {EmployeeService} from "../../../zep-services/employee.service";
import {SocialUser} from "angularx-social-login";

@Injectable({
  providedIn: 'root'
})
export class DisplayEmployeesService {

  private _selectedEmployees: BehaviorSubject<Array<Employee>> = new BehaviorSubject<Array<Employee>>(new Array<Employee>());

  private _resetSelection: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  getEmployees(user: SocialUser): Observable<Array<Employee>> {
    return this.employeeService.getAll(user);
  }

  updateEmployees(employees: Array<Employee>, date: string) {
    employees.forEach(empl => empl.freigabedatum = date);
    return this.employeeService.updateAll(employees);
  }


  get selectedEmployees(): BehaviorSubject<Array<Employee>> {
    return this._selectedEmployees;
  }

  setSelectedEmployees(value: SelectionChange<Employee>) {
    let employees: Array<Employee> = this.selectedEmployees.getValue();

    if (value != null) {
      for (let employee of value.added) {
        employees.push(employee);
      }

      for (let empl of value.removed) {
        let index = employees.findIndex(d => d.userId === empl.userId);
        // remove element from array
        employees.splice(index, 1);
      }
    }
    this.selectedEmployees.next(employees);
  }

  get resetSelection(): BehaviorSubject<boolean> {
    return this._resetSelection;
  }

  setResetSelection(value: boolean) {
    this._resetSelection.next(value);
    this.selectedEmployees.next(new Array<Employee>());
  }
}

