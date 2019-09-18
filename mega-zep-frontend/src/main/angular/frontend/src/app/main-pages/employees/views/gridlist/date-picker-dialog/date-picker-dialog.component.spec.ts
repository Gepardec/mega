import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';

import {DatePickerDialogComponent} from './date-picker-dialog.component';
import {AngularMaterialModule} from "../../../../../material-module";
import {EmployeesPagesModule} from "../../../home/home.module";
import {MAT_DIALOG_DATA} from "@angular/material";
import {HttpClientTestingModule} from "@angular/common/http/testing";


describe('DatePickerDialogComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        AngularMaterialModule,
        EmployeesPagesModule],
      declarations: [],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DatePickerDialogComponent> = TestBed.createComponent(DatePickerDialogComponent);
    const app: DatePickerDialogComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
      const {fixture, app} = setup();
      expect(app).toBeTruthy();
  });
});
