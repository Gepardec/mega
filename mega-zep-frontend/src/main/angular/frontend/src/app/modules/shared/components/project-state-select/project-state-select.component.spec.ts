import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectStateSelectComponent} from './project-state-select.component';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {ProjectState} from '../../models/ProjectState';
import {AngularMaterialModule} from '../../../material/material-module';

const PROJECT_STATES_LENGTH = Object.keys(ProjectState).length;
const STATE_PREFIX = 'STATE.';

describe('ProjectStateSelectComponent', () => {

  let fixture: ComponentFixture<ProjectStateSelectComponent>;
  let component: ProjectStateSelectComponent;

  let translateService: TranslateService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProjectStateSelectComponent
      ],
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

    const optionDone = options.find(option => option.value === ProjectState.DONE);
    expect(component.isDoneSelected).toBeFalse();

    optionDone.select();
    fixture.detectChanges();
    flush();

    expect(component.isDoneSelected).toBeTrue();
    expect(component.select.disabled).toBeTrue();
  }));

  it('#selectStateWorkInProgress - should disable option OPEN when WIP is selected', fakeAsync(() => {
    fixture.detectChanges();

    component.select.open();
    fixture.detectChanges();
    flush();

    const options = component.select.options;
    expect(options.length).toBe(PROJECT_STATES_LENGTH);

    const optionWip = options.find(option => option.value === ProjectState.WORK_IN_PROGRESS);
    expect(component.isInProgressSelected).toBeFalse();

    optionWip.select();
    fixture.detectChanges();
    flush();

    expect(component.isInProgressSelected).toBeTrue();
    expect(component.select.disabled).toBeFalse();
    expect(options.find(option => option.value === ProjectState.OPEN).disabled).toBeTrue();
    expect(options.find(option => option.value === ProjectState.DONE).disabled).toBeFalse();
    expect(options.find(option => option.value === ProjectState.NOT_RELEVANT).disabled).toBeFalse();
  }));

  it('#selectNotRelevant - should disable options WIP and DONE when NOT_RELEVANT is selected', fakeAsync(() => {
    fixture.detectChanges();

    component.select.open();
    fixture.detectChanges();
    flush();

    const options = component.select.options;
    expect(options.length).toBe(PROJECT_STATES_LENGTH);

    const optionNotRelevant = options.find(option => option.value === ProjectState.NOT_RELEVANT);
    expect(component.isNotRelevantSelected).toBeFalse();

    optionNotRelevant.select();
    fixture.detectChanges();
    flush();

    expect(component.isNotRelevantSelected).toBeTrue();
    expect(component.select.disabled).toBeFalse();
    expect(options.find(option => option.value === ProjectState.WORK_IN_PROGRESS).disabled).toBeTrue();
    expect(options.find(option => option.value === ProjectState.DONE).disabled).toBeTrue();
    expect(options.find(option => option.value === ProjectState.OPEN).disabled).toBeFalse();
  }));
});
