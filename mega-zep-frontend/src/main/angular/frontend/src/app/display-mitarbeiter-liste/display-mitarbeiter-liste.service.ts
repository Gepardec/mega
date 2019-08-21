import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {MitarbeiterResponseType} from "../models/Mitarbeiter/MitarbeiterResponseType";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, retry} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DisplayMitarbeiterListeService {

  // Base url
  baseurl = 'http://localhost:8080';

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

}
