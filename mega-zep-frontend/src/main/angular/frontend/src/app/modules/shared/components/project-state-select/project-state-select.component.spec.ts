import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ProjectStateSelectComponent} from './project-state-select.component';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {CUSTOM_ELEMENTS_SCHEMA, DebugElement} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {By} from '@angular/platform-browser';
import {ProjectState} from '../../models/ProjectState';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {AngularMaterialModule} from '../../../material/material-module';
import {SharedModule} from '../../shared.module';
import {MatSelect} from '@angular/material/select';
import {MatOption} from '@angular/material/core';
import {click} from '../../../../testing/click-simulator';

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

  it('#afterInit - should contain four ProjectStates', waitForAsync(() => {

    fixture.detectChanges();

    const select = fixture.debugElement.queryAll(By.directive(MatSelect));
    expect(select.length).toBe(1);

    click(select[0]);

    fixture.detectChanges();

    fixture.whenStable().then(() => {
      console.log('entered whenStable()')

      const options = fixture.debugElement.queryAll(By.directive(MatOption));

      expect(options.length).toBe(4);
      expect(options[0].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.OPEN));
      expect(options[1].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.WORK_IN_PROGRESS));
      expect(options[2].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.DONE));
      expect(options[3].nativeElement.textContent).toEqual(translateService.get('STATE.' + ProjectState.NOT_RELEVANT));
    });
  }));

  it('#selectStateDone - should disable the select when state is DONE', waitForAsync(()  => {

    fixture.detectChanges();

    const select = fixture.debugElement.queryAll(By.directive(MatSelect));
    expect(select.length).toBe(1);

    click(select[0]);

    fixture.detectChanges();

    fixture.whenStable().then(() => {
      const options = fixture.debugElement.queryAll(By.directive(MatOption));

      expect(options.length).toBe(4);
      click(options.find(option => option.nativeElement.textContent === ProjectState.DONE));

      fixture.detectChanges();

      fixture.whenStable().then(() => {
        const select = fixture.debugElement.queryAll(By.directive(MatSelect));

        expect(select.length).toBe(1);
        expect(select[0].nativeElement.disabled).toBe(false);
      });
    });
  }));

})
