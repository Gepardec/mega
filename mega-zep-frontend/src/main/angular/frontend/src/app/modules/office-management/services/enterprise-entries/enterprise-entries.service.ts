import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ConfigService} from '../../../shared/services/config/config.service';
import {ProjectState} from '../../../shared/models/ProjectState';
import {Observable, of} from 'rxjs';
import {EnterpriseEntry} from '../../models/EnterpriseEntry';
import {ProjectManagementEntry} from '../../../project-management/models/ProjectManagementEntry';
import {take} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EnterpriseEntriesService {

  private enterpriseEntryMock = new EnterpriseEntry(ProjectState.OPEN, ProjectState.WORK_IN_PROGRESS, ProjectState.DONE, ProjectState.NOT_RELEVANT, '06-2021');

  constructor(private httpClient: HttpClient,
              private config: ConfigService) {
  }

  getEnterpriseEntry(year: number, month: number): Observable<EnterpriseEntry> {
    // return this.httpClient.get<EnterpriseEntry>(
    //   this.config.getBackendUrlWithContext('/enterprise/entriesformonthyear/' + year + '/' + month));
    //FIXME: Mock
    return of(this.enterpriseEntryMock);
  }

  updateEnterpriseEntry(enterpriseEntry: EnterpriseEntry): Observable<boolean> {
    // this.eeEntry = enterpriseEntry;
    this.httpClient.get<Array<ProjectManagementEntry>>(this.config.getBackendUrlWithContext('/management/projectmanagemententries/' + 2021 + '/' + 4)).subscribe();
    return of(false);
    // FIXME: Mock
    // return this.httpClient.put<boolean>(
    //   this.config.getBackendUrlWithContext('/enterpriseentry'), enterpriseEntry);
  }
}
