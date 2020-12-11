import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EmployeeProgressComponent} from './employee-progress.component';
import {AngularMaterialModule} from "../../../../material/material-module";
import {TranslateModule} from "@ngx-translate/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SharedModule} from "../../../../shared/shared.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";

describe('EmployeeProgressComponent', () => {
  let component: EmployeeProgressComponent;
  let fixture: ComponentFixture<EmployeeProgressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeProgressComponent],
      imports: [
        BrowserAnimationsModule,
        SharedModule,
        AngularMaterialModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        RouterTestingModule
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeProgressComponent);
    component = fixture.componentInstance;
    component.employeeProgresses = []

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
