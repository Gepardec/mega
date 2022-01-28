import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectStateSelectComponent} from './project-state-select.component';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {ProjectState} from '../../models/ProjectState';
import {AngularMaterialModule} from '../../../material/material-module';

const PROJECT_STATES_LENGTH = Object.keys(ProjectState).length;
const STATE_PREFIX = 'STATE.';

fdescribe('ProjectStateSelectComponent', () => {

  let fixture: ComponentFixture<ProjectStateSelectComponent>;
  let component: ProjectStateSelectComponent;

  let translateService: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot(),
        AngularMaterialModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ProjectStateSelectComponent);
      component = fixture.componentInstance;
      translateService = TestBed.inject(TranslateService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should create select', fakeAsync(() => {
    expect(component.select).toBeFalsy();

    fixture.detectChanges();

    expect(component.select).toBeTruthy();
  }));

  it('#afterInit - should contain four ProjectStates', fakeAsync(() => {
    fixture.detectChanges();

    component.select.open();
    fixture.detectChanges();
    flush();

    const options = component.select.options;
    expect(options.length).toBe(PROJECT_STATES_LENGTH);

    options.forEach((option, index) => {
      // checks if the text of the option equals the translated value
      translateService.get(STATE_PREFIX + Object.keys(ProjectState)[index]).subscribe(value => {
        expect(option._getHostElement().innerText).toEqual(value);
      });
    });
  }));

  it('#selectStateDone - should disable the select when state is DONE', fakeAsync(() => {
    fixture.detectChanges();

    component.select.open();
    fixture.detectChanges();
    flush();

    const options = component.select.options;
    expect(options.length).toBe(PROJECT_STATES_LENGTH);

    const optionDone = options.filter(option => option.value === ProjectState.DONE)[0];
    expect(component.isDoneSelected).toBeFalse();

    optionDone.select();
    fixture.detectChanges();
    flush();

    expect(component.isDoneSelected).toBeTrue();
    expect(component.select.disabled).toBeTrue();
  }));
});
