import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, retry} from 'rxjs/operators';
import {SelectionChange} from "@angular/cdk/collections";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {configuration} from "../../../../configuration/configuration";
import {ErrorHandleService} from "../../error-handle.service";

@Injectable({
  providedIn: 'root'
})
export class DisplayEmployeeListService {

  private URL: string = configuration.BASEURL;

  private _selectedEmployees: BehaviorSubject<Array<MitarbeiterType>> =
    new BehaviorSubject<Array<MitarbeiterType>>(new Array<MitarbeiterType>());

  constructor(
    private http: HttpClient,
    private errorHandlService: ErrorHandleService
  ) {
  }

  // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  getMitarbeiter(data): Observable<MitarbeiterResponseType> {
    return this.http.post<MitarbeiterResponseType>(this.URL +
      '/worker/getAll/', JSON.stringify(data), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandlService.errorHandl)
      );
  }

  updateMitarbeiter(employees: Array<MitarbeiterType>) {
    return this.http.put(this.URL +
      '/worker/update/', JSON.stringify(employees),
      this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandlService.errorHandl)
      );
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
