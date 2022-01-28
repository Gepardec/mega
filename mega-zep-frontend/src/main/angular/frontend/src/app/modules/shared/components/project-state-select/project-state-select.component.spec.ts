import {async, ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectStateSelectComponent} from './project-state-select.component';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {By} from '@angular/platform-browser';
import {ProjectState} from '../../models/ProjectState';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {AngularMaterialModule} from '../../../material/material-module';
import {MatOption} from '@angular/material/core';
import {SharedModule} from '../../shared.module';

fdescribe('ProjectStateSelectComponent', () => {

  let fixture: ComponentFixture<ProjectStateSelectComponent>;
  let component: ProjectStateSelectComponent;
  let translateService: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectStateSelectComponent],
      imports: [
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        NoopAnimationsModule,
        AngularMaterialModule,
        SharedModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(ProjectStateSelectComponent);
        component = fixture.componentInstance;
        translateService = TestBed.inject(TranslateService);
      });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should contain four ProjectStates', () => {

    fixture.detectChanges();

    console.log(fixture.debugElement.children);
    let dropdownElements = fixture.debugElement.queryAll(By.css('.mat-select'));

    expect(dropdownElements.length).toBe(4);
    expect(dropdownElements[0].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.OPEN));
    expect(dropdownElements[1].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.WORK_IN_PROGRESS));
    expect(dropdownElements[2].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.DONE));
    expect(dropdownElements[3].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.NOT_RELEVANT));


  });
});
