import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {JourneyCheckComponent} from './journey-check.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('JourneyCheckComponent', () => {
  let component: JourneyCheckComponent;
  let fixture: ComponentFixture<JourneyCheckComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot(),
        AngularMaterialModule,
        HttpClientTestingModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(JourneyCheckComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#getJourneyWarningString - should set warnings', () => {
    fixture.detectChanges();

    const warnings: string = component.getJourneyWarningString(WarningsMock.warnings);

    WarningsMock.warnings.forEach(warning => {
      expect(warnings).toContain(warning);
    })
  });

  class WarningsMock {
    static warnings: Array<string> = ['do', 'something'];
  }
});
