import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ConfigService} from '../config/config.service';
import {Observable} from 'rxjs';
import {ProjectComment} from '../../models/ProjectComment';

@Injectable({
  providedIn: 'root'
})
export class ProjectCommentService {

  constructor(private httpClient: HttpClient,
              private configService: ConfigService) {
  }

  get(monthYear: string, projectName: string) {
    return this.httpClient.get<ProjectComment>(
      this.configService.getBackendUrlWithContext('/projectcomments'), {
        params: {
          date: monthYear,
          projectName: projectName
        }
      });
  }

  create(comment: string, yearMonth: string, projectName: string) {
    return this.httpClient.post<ProjectComment>(
      this.configService.getBackendUrlWithContext('/projectcomments'),
      new ProjectComment(comment, yearMonth, projectName));
  }

  update(projectComment: ProjectComment): Observable<boolean> {
    return this.httpClient.put<boolean>(
      this.configService.getBackendUrlWithContext('/projectcomments'),
      projectComment
    );
  }
}
