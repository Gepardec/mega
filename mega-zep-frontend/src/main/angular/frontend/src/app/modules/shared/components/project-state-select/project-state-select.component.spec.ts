import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectStateSelectComponent } from './project-state-select.component';

describe('ProjectStateSelectComponent', () => {
  let component: ProjectStateSelectComponent;
  let fixture: ComponentFixture<ProjectStateSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectStateSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectStateSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
