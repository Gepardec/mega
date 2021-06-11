import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectStateSelectComponent } from './project-state-select.component';
import {TranslateModule} from "@ngx-translate/core";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";

describe('ProjectStateSelectComponent', () => {
  let component: ProjectStateSelectComponent;
  let fixture: ComponentFixture<ProjectStateSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectStateSelectComponent ],
      imports: [TranslateModule.forRoot()],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
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
