import {Injectable} from '@angular/core';
import {ConfigService} from '../../shared/services/config/config.service';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {ProjectManagementEntry} from '../models/ProjectManagementEntry';
import {ProjectState} from '../../shared/models/ProjectState';

@Injectable({
  providedIn: 'root'
})
export class ProjectManagementService {

  resetProjectStateSelect = new Subject<ProjectState>();

  constructor(private configService: ConfigService, private httpClient: HttpClient) { }

  getEntries(year: number, month: number): Observable<Array<ProjectManagementEntry>> {
    return this.httpClient.get<Array<ProjectManagementEntry>>(
      this.configService.getBackendUrlWithContext('/management/projectmanagemententries/' + year + '/' + month)
    );
  }
}
