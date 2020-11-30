import {Injectable} from '@angular/core';
import {Employee} from '../../models/Employee';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ConfigService} from '../config/config.service';
import {EmployeeStep} from '../../models/EmployeeStep';
import {Step} from '../../models/Step';
import {ProjectStep} from '../../models/ProjectStep';

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

  closeProjectCheck(employee: Employee, projectName: string): Observable<boolean> {
    return this.httpClient.put<boolean>(
      this.config.getBackendUrlWithContext('/stepentry/closeforproject'),
      new ProjectStep(Step.CONTROL_TIME_EVIDENCES, employee, projectName)
    );
  }
}
