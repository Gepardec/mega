import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectOverviewCardComponent } from './project-overview-card.component';

describe('ProjectOverviewCardComponent', () => {
  let component: ProjectOverviewCardComponent;
  let fixture: ComponentFixture<ProjectOverviewCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectOverviewCardComponent ]
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
