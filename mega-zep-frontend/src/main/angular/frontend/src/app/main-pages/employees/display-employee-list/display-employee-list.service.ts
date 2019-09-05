import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {SelectionChange} from "@angular/cdk/collections";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {EmployeeService} from "../../../zep-services/employee.service";
import {SocialUser} from "angularx-social-login";

@Injectable({
  providedIn: 'root'
})
export class DisplayEmployeeListService {

  private _selectedEmployees: BehaviorSubject<Array<MitarbeiterType>> =
    new BehaviorSubject<Array<MitarbeiterType>>(new Array<MitarbeiterType>());

  private _resetSelection: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  getEmployees(user: SocialUser): Observable<MitarbeiterResponseType> {
    return this.employeeService.getAll(user);
  }

  updateEmployees(employees: Array<MitarbeiterType>, date: string) {
    employees.forEach(empl => empl.freigabedatum = date);
    return this.employeeService.updateAll(employees);
  }


  get selectedEmployees(): BehaviorSubject<Array<MitarbeiterType>> {
    return this._selectedEmployees;
  }

  setSelectedEmployees(value: SelectionChange<MitarbeiterType>) {
    let employees: Array<MitarbeiterType> = [];
    employees = this.selectedEmployees.getValue();
    if (value != null) {
      for (let empl of value.added) {
        employees.push(empl);
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
    this.selectedEmployees.next(new Array<MitarbeiterType>());
  }
}

