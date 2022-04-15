import {Injectable} from '@angular/core';
import {ConfigService} from '../../shared/services/config/config.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProjectManagementEntry} from '../models/ProjectManagementEntry';

@Injectable({
  providedIn: 'root'
})
export class ProjectManagementService {

  constructor(private configService: ConfigService, private httpClient: HttpClient) {
  }

  getEntries(year: number, month: number, all: boolean): Observable<Array<ProjectManagementEntry>> {
    const params: HttpParams = new HttpParams().append('all', `${all}`);

    return this.httpClient.get<Array<ProjectManagementEntry>>(
      this.configService.getBackendUrlWithContext('/management/projectmanagemententries/' + year + '/' + month),
      {
        params: params
      });
  }
}
