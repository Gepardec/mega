import {TestBed} from '@angular/core/testing';

import {ProjectCommentService} from './project-comment.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {ConfigService} from '../config/config.service';
import {ProjectComment} from '../../models/ProjectComment';
import {HttpResponse} from '@angular/common/http';

describe('ProjectCommentService', () => {

  let projectCommentService: ProjectCommentService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    projectCommentService = TestBed.inject(ProjectCommentService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(projectCommentService).toBeTruthy();
  });

  it('#get - should get ProjectComment', (done) => {
    projectCommentService.get(ProjectCommentMock.monthYear, ProjectCommentMock.projectName)
      .subscribe(projectComment => {
        expect(projectComment).toEqual(ProjectCommentMock.projectComment);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext(`/projectcomments?date=${ProjectCommentMock.monthYear}&projectName=${ProjectCommentMock.projectName}`));
    testRequest.flush(ProjectCommentMock.projectComment);
  });

  it('#create - should return ProjectComment', (done) => {
    projectCommentService.create(ProjectCommentMock.comment, ProjectCommentMock.monthYear, ProjectCommentMock.projectName)
      .subscribe(projectComment => {
        expect(projectComment).toEqual(ProjectCommentMock.projectComment);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/projectcomments'));
    testRequest.flush(ProjectCommentMock.projectComment);
  });

  it('#update - should return true', (done) => {
    projectCommentService.update(ProjectCommentMock.projectComment)
      .subscribe(success => {
        expect(success).toEqual(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/projectcomments'));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  class ProjectCommentMock {

    static comment: string = 'this is a comment';
    static monthYear: string = '2021-11';
    static projectName: string = 'LIW-Microservices'

    static projectComment: ProjectComment = {
      comment: ProjectCommentMock.comment,
      projectName: ProjectCommentMock.projectName,
      id: 1,
      date: '2021-11-23'
    }
  }
});
