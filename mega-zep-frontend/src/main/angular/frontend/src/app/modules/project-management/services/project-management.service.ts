import { Injectable } from '@angular/core';
import {ConfigService} from '../../shared/services/config/config.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProjectManagementEntry} from '../models/ProjectManagementEntry';

@Injectable({
  providedIn: 'root'
})
export class ProjectManagementService {

  constructor(private configService: ConfigService, private httpClient: HttpClient) { }

  getEntries(): Observable<Array<ProjectManagementEntry>> {
    return this.httpClient.get<Array<ProjectManagementEntry>>(
      this.configService.getBackendUrlWithContext('/management/projectmanagemententries')
    );
  }
}
