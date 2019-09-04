import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {HttpClient} from "@angular/common/http";
import {retry} from 'rxjs/operators';
import {SelectionChange} from "@angular/cdk/collections";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {configuration} from "../../../../configuration/configuration";
import {EmployeeService} from "../../../zep-services/employee.service";
import {SocialUser} from "angularx-social-login";

@Injectable({
  providedIn: 'root'
})
export class DisplayEmployeeListService {

  private _selectedEmployees: BehaviorSubject<Array<MitarbeiterType>> =
    new BehaviorSubject<Array<MitarbeiterType>>(new Array<MitarbeiterType>());

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  getEmployees(user: SocialUser): Observable<MitarbeiterResponseType> {
    return this.employeeService.getAll(user);
  }

  updateEmployee(employees: Array<MitarbeiterType>) {
    return this.employeeService.updateAll(employees);
  }


  get selectedEmployees(): BehaviorSubject<Array<MitarbeiterType>> {
    return this._selectedEmployees;
  }

  setSelectedEmployees(value: SelectionChange<MitarbeiterType>) {
    let employees: Array<MitarbeiterType> = [];
    employees = this.selectedEmployees.getValue();
    for (let empl of value.added) {
      employees.push(empl);
    }

    for (let empl of value.removed) {
      let index = employees.findIndex(d => d.userId === empl.userId);
      // remove element from array
      employees.splice(index, 1);
    }
    this.selectedEmployees.next(employees);
  }
}
