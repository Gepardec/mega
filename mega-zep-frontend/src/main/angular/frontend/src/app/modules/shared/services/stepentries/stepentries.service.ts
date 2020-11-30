import {Injectable} from '@angular/core';
import {Employee} from '../../models/Employee';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ConfigService} from '../config/config.service';
import {EmployeeStep} from '../../models/EmployeeStep';
import {Step} from '../../models/Step';

@Injectable({
  providedIn: 'root'
})
export class StepentriesService {

  constructor(
    private httpClient: HttpClient,
    private config: ConfigService
  ) {
  }

  close(employee: Employee, step: Step): Observable<boolean> {
    return this.httpClient.put<boolean>(
      this.config.getBackendUrlWithContext('/stepentry/close'),
      new EmployeeStep(step, employee)
    );
  }
}
