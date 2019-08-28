import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {MitarbeiterResponseType} from "../../models/Mitarbeiter/MitarbeiterResponseType";
import {catchError, retry} from "rxjs/operators";
import {configuration} from "../../../configuration/configuration";
import {ErrorHandleService} from "../error-handle.service";
import {MitarbeiterType} from "../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  URL: string = configuration.BASEURL;

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

  getMitarbeiter(data): Observable<MitarbeiterType> {
    return this.http.post<MitarbeiterType>(this.URL +
      '/worker/get/', JSON.stringify(data), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.errorHandlService.errorHandl)
      );
  }
}
