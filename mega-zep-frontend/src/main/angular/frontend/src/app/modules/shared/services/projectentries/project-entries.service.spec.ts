import {TestBed} from '@angular/core/testing';

import {ProjectEntriesService} from './project-entries.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ConfigService} from "../config/config.service";
import {HttpResponse} from "@angular/common/http";
import {ProjectState} from "../../models/ProjectState";
import {ProjectStepQualifier} from "../../../project-management/models/ProjectEntry";

describe('ProjectEntriesService', () => {

  let projectEntriesService: ProjectEntriesService;
  let httpTestingController: HttpTestingController;
  let configService: ConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    projectEntriesService = TestBed.inject(ProjectEntriesService);
    httpTestingController = TestBed.inject(HttpTestingController);
    configService = TestBed.inject(ConfigService);
  });

  it('#should be created', () => {
    expect(projectEntriesService).toBeTruthy();
  });

  it('#updateProjectEntry - should return true', (done) => {
    projectEntriesService.updateProjectEntry(ProjectEntryMock.state, ProjectEntryMock.preset, ProjectEntryMock.projectName, ProjectEntryMock.step, ProjectEntryMock.monthYear)
      .subscribe(success => {
        expect(success).toEqual(true);
        done();
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/projectentry'));
    testRequest.event(new HttpResponse<boolean>({body: true}));
  });

  class ProjectEntryMock {

    static state: ProjectState = ProjectState.DONE;
    static preset: boolean = true;
    static projectName: string = 'LIW-Microservices';
    static step: ProjectStepQualifier = 'CONTROL_PROJECT';
    static monthYear: string = '2021-11';

  }
});
