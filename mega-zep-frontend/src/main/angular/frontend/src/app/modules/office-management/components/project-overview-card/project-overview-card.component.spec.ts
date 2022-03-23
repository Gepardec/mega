import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProjectOverviewCardComponent} from './project-overview-card.component';
import {TranslateModule} from "@ngx-translate/core";
import {OfficeManagementModule} from "../../office-management.module";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ProjectOverviewCardComponent', () => {
  let component: ProjectOverviewCardComponent;
  let fixture: ComponentFixture<ProjectOverviewCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule.forChild(),
        OfficeManagementModule,
        RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectOverviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
