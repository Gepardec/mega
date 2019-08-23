import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, retry} from 'rxjs/operators';
import {SelectionChange} from "@angular/cdk/collections";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Injectable({
  providedIn: 'root'
})
export class DisplayEmployeeListService {

  // Base url
  baseurl = 'http://localhost:8080';

  private _selectedEmployees: BehaviorSubject<Array<MitarbeiterType>> =
    new BehaviorSubject<Array<MitarbeiterType>>(new Array<MitarbeiterType>());

  constructor(
    private http: HttpClient
  ) {
  }

  // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  getMitarbeiter(data): Observable<MitarbeiterResponseType> {
    return this.http.post<MitarbeiterResponseType>(this.baseurl +
      '/mega-zep-1.0.0-SNAPSHOT/rest-api/worker/get/', JSON.stringify(data), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandl)
      );
  }

  updateMitarbeiter(employees: Array<MitarbeiterType>) {
    return this.http.put(this.baseurl +
      '/mega-zep-1.0.0-SNAPSHOT/rest-api/worker/update/', JSON.stringify(employees),
      this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandl)
      );
  }

  // Error handling
  errorHandl(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
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
