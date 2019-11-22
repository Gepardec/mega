import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DisplayEmployeesComponent} from './display-employees.component';
import {AngularMaterialModule} from "../../../material-module";
import {EmployeesPagesModule} from "../home/home.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {AppModule} from "../../../app.module";
import {RouterTestingModule} from "@angular/router/testing";
import {routes} from "../../../app-routing.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";
import {By} from "@angular/platform-browser";
import {EmployeeResponseType} from "../../../models/Employee/EmployeeResponseType";
import {Employee} from "../../../models/Employee/Employee";
import {DebugElement} from "@angular/core";

describe('DisplayEmployeeListComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AngularMaterialModule,
        EmployeesPagesModule,
        HttpClientTestingModule,
        AppModule,
        RouterTestingModule.withRoutes(routes)
      ],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DisplayEmployeesComponent> = TestBed.createComponent(DisplayEmployeesComponent);
    const app: DisplayEmployeesComponent = fixture.debugElement.componentInstance;
    fixture.detectChanges();


    return {fixture, app};
  }

  function setEmployees(empl: EmployeeResponseType): EmployeeResponseType {
    let emplType = new Employee();
    emplType.vorname = "Max";
    emplType.nachname = "Mustermann";
    emplType.email = "max.mustermann@mustermail.com";
    empl.employeeList = Array<Employee>(emplType);
    empl.length = 1;
    return empl;
  }


  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should be visible DatePickerComponent', () => {
    const {fixture, app} = setup();
    app.isGridlistActive = false;
    //app.employees = new EmployeeResponseType();
    app.selectedEmployees = new Array<Employee>(5);
    //setEmployees(app.employees);

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css("#app-datepicker"))).not.toBeNull();
  });

  it('should not be visible DatePickerComponent', () => {
    const {fixture, app} = setup();
    app.isGridlistActive = true;

    expect(fixture.debugElement.query(By.css("#app-datepicker"))).toBeNull();
  });

  it('should be visible Release Times Button', () => {
    const {fixture, app} = setup();
    app.isGridlistActive = false;
    app.selectedEmployees = new Array<Employee>(5);
    app.selectedDate = "2019-09-13";
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css("#releaseTimesBtn"))).not.toBeNull();
    expect(fixture.debugElement.nativeElement.querySelector("#releaseTimesBtn").textContent).toEqual("Freigeben");
  });

  it('should not be visible Release Times Button', () => {
    const {fixture, app} = setup();
    app.isGridlistActive = true;
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css("#releaseTimesBtn"))).toBeNull();
  });

  it('should toggle view', () => {
    const {fixture, app} = setup();
    app.isGridlistActive = false;
    //app.employees = new EmployeeResponseType();
    app.selectedEmployees = new Array<Employee>(5);
    //setEmployees(app.employees);
    fixture.detectChanges();

    let appDatepicker: DebugElement = fixture.debugElement.query(By.css("#app-datepicker"));
    let appPagination: DebugElement = fixture.debugElement.query(By.css("#pagination"));
    expect(appDatepicker.nativeElement).not.toBeNull();

    app.toggleView();
    fixture.detectChanges();

    appDatepicker = fixture.debugElement.query(By.css("#app-datepicker"));
    appPagination = fixture.debugElement.query(By.css("#pagination"));
    expect(appDatepicker).toBeNull();
    expect(appPagination.nativeElement).not.toBeNull();
  });
});

