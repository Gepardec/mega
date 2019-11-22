import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DatePickerDialogComponent} from './date-picker-dialog.component';
import {AngularMaterialModule} from "../../../../../material-module";
import {EmployeesPagesModule} from "../../../home/home.module";
import {MAT_DIALOG_DATA} from "@angular/material";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {Employee} from "../../../../../models/Employee/Employee";
import {UtilService} from "../../../../../shared/util/util.service";
import {Observable} from "rxjs";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";


describe('DatePickerDialogComponent', () => {

  let employee: Employee = new Employee();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        HttpClientTestingModule,
        AngularMaterialModule,
        EmployeesPagesModule],
      declarations: [],
      providers: [
        {provide: MAT_DIALOG_DATA, useValue: {}}
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DatePickerDialogComponent> = TestBed.createComponent(DatePickerDialogComponent);
    const app: DatePickerDialogComponent = fixture.debugElement.componentInstance;

    setEmployeeData();

    return {fixture, app};
  }

  function setEmployeeData() {
    employee.vorname = "Max";
    employee.nachname = "Mustermann";
    employee.email = "max.mustermann@mustermann.at";
    employee.freigabedatum = "2019-01-01";
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should set freigabedatum correctly', () => {
    const {fixture, app} = setup();
    let utilService: UtilService = fixture.debugElement.injector.get(UtilService);

    spyOn(utilService, 'updateEmployees').and.returnValue(new Observable<Response>());
    let date = "2019-12-31";
    app.date = date;

    expect(employee.freigabedatum).not.toEqual(date);

    app.updateEmployee(employee);
    fixture.detectChanges();

    expect(employee.freigabedatum).toEqual(date);
  });

  it('should open snackbar', () => {
    const {fixture, app} = setup();
    let snackbar = app.openSnackBar("Test: .oOo.");
    expect(snackbar).not.toBeNull();
  });
});
